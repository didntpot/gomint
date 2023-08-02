package io.gomint.server.network.handler;

import io.gomint.entity.Entity;
import io.gomint.event.player.PlayerDropItemEvent;
import io.gomint.event.player.PlayerExhaustEvent;
import io.gomint.event.player.PlayerInteractEvent;
import io.gomint.event.player.PlayerInteractWithEntityEvent;
import io.gomint.event.world.BlockBreakEvent;
import io.gomint.inventory.item.ItemStack;
import io.gomint.inventory.item.ItemSword;
import io.gomint.math.Vector;
import io.gomint.server.entity.EntityPlayer;
import io.gomint.server.inventory.ContainerInventory;
import io.gomint.server.inventory.Inventory;
import io.gomint.server.inventory.item.ItemBow;
import io.gomint.server.inventory.item.ItemCrossbow;
import io.gomint.server.inventory.transaction.DropItemTransaction;
import io.gomint.server.inventory.transaction.InventoryTransaction;
import io.gomint.server.inventory.transaction.TransactionGroup;
import io.gomint.server.network.PlayerConnection;
import io.gomint.server.network.packet.PacketInventoryTransaction;
import io.gomint.server.network.packet.types.transaction.*;
import io.gomint.server.plugin.EventCaller;
import io.gomint.server.util.Values;
import io.gomint.server.world.block.Block;
import io.gomint.world.Gamemode;
import io.gomint.world.block.BlockAir;
import io.gomint.world.block.data.Facing;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketInventoryTransactionHandler implements PacketHandler<PacketInventoryTransaction> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketInventoryTransactionHandler.class);

    private final EventCaller eventCaller;

    public PacketInventoryTransactionHandler(EventCaller eventCaller) {
        this.eventCaller = eventCaller;
    }

    @Override
    public void handle(PacketInventoryTransaction packet, long currentTimeMillis, PlayerConnection connection) {
        TransactionData trData = packet.getTrData();
        // Interact / Build
        if (trData instanceof NormalTransactionData) {
            this.handleTypeNormal(connection, packet);
        } else if (trData instanceof UseItemTransactionData || trData instanceof ReleaseItemTransactionData) { // Consume / use item
            // Sanity checks before we interact with worlds and their chunk loading
            Vector packetPosition;
            if (trData instanceof UseItemTransactionData) {
                packetPosition = ((UseItemTransactionData) trData).getPlayerPosition();
            } else {
                packetPosition = ((ReleaseItemTransactionData) trData).getHeadPosition();
            }
            Vector playerPosition = connection.entity().position();

            double distance = packetPosition.distanceSquared(playerPosition);
            double offsetLimit = 0.5;
            if (distance > offsetLimit) {
                Block block = connection.entity().world().blockAt(playerPosition.toBlockPosition());
                LOGGER.warn("Mismatching position: {} -> {} / {}", distance, playerPosition, block.getClass().getSimpleName());
                reset(packet, connection);
                return;
            }

            // Special case in air clicks
            if (trData instanceof UseItemTransactionData && ((UseItemTransactionData) trData).getActionType() != 1) {
                // Check if we can interact
                Vector interactCheckVector = ((UseItemTransactionData) trData).getBlockPosition().toVector().add(.5f, .5f, .5f);
                if (!connection.entity().canInteract(interactCheckVector, 13) ||
                    connection.entity().gamemode() == Gamemode.SPECTATOR) {
                    LOGGER.warn("Can't interact from position: {} / {}", connection.entity().position(), ((UseItemTransactionData) trData).getBlockPosition());
                    reset(packet, connection);
                    return;
                }
            }

            // Check if in hand item is in sync
            ItemStack<?> itemInHand = connection.entity().inventory().itemInHand();
            ItemStack<?> packetItemInHand;
            if (trData instanceof UseItemTransactionData) {
                packetItemInHand = ((UseItemTransactionData) trData).getItemInHand();
            } else {
                packetItemInHand = ((ReleaseItemTransactionData) trData).getItemInHand();
            }
            if (itemInHand.itemType() != packetItemInHand.itemType()) {
                LOGGER.debug("{} item in hand does not match: {} / {}", connection.entity().name(), itemInHand, packetItemInHand);
                reset(packet, connection);
                return;
            }

            if (trData instanceof UseItemTransactionData) {
                this.handleUseItem(itemInHand, connection, packet, (UseItemTransactionData) trData);
            } else {
                this.handleConsumeItem(itemInHand, connection, packet, (ReleaseItemTransactionData) trData);
            }
        } else if (trData instanceof UseItemOnEntityTransactionData) {
            ItemStack<?> packetItemInHand;
            ItemStack<?> itemInHand;// Check item in hand
            itemInHand = connection.entity().inventory().itemInHand();
            packetItemInHand = ((UseItemOnEntityTransactionData) trData).getItemInHand();
            if (itemInHand.itemType() != packetItemInHand.itemType()) {
                LOGGER.warn("{} item in hand does not match (X): {} / {}", connection.entity().name(), itemInHand, packetItemInHand);
                reset(packet, connection);
                return;
            }

            // When the player wants to do this it should have selected a entity in its interact
            if (connection.entity().hoverEntity() == null) {
                LOGGER.warn("{} selected entity is null", connection.entity().name());
                reset(packet, connection);
                return;
            }

            // Find the entity from this packet
            long entityId = ((UseItemOnEntityTransactionData) trData).getEntityRuntimeId();
            Entity<?> entity = connection.entity().world().findEntity(entityId);
            if (!connection.entity().hoverEntity().equals(entity)) {
                LOGGER.warn("{} entity does not match: {}; {}; {}", connection.entity().name(), entity, connection.entity().hoverEntity(), entityId);
                reset(packet, connection);
                return;
            }

            // Fast check for interact rules
            Vector interactCheckVector = ((UseItemOnEntityTransactionData) trData).getPlayerPosition().add(.5f, .5f, .5f);
            if (!connection.entity().canInteract(interactCheckVector, 8) ||
                connection.entity().gamemode() == Gamemode.SPECTATOR) {
                LOGGER.warn("Can't interact from position");
                reset(packet, connection);
                return;
            }

            this.handleUseItemOnEntity(entity, connection, packet, (UseItemOnEntityTransactionData) trData);
        } else {
            connection.entity().setUsingItem(false);
        }
    }

    private void handleUseItemOnEntity(
        io.gomint.entity.Entity<?> target,
        PlayerConnection connection,
        PacketInventoryTransaction packet,
        UseItemOnEntityTransactionData trData
    ) {
        switch (trData.getActionType()) {
            case UseItemOnEntityTransactionData.ACTION_INTERACT:     // Interact
                io.gomint.entity.Entity<?> entity = connection.entity().world().findEntity(trData.getEntityRuntimeId());

                PlayerInteractWithEntityEvent event = new PlayerInteractWithEntityEvent(connection.entity(), entity);

                connection.server().pluginManager().callEvent(event);

                if (event.cancelled()) {
                    break;
                }

                entity.interact(connection.entity(), trData.getPlayerPosition());

                break;
            case UseItemOnEntityTransactionData.ACTION_ATTACK:     // Attack
                if (connection.entity().attackWithItemInHand(target)) {
                    if (connection.entity().gamemode() != Gamemode.CREATIVE) {
                        ItemStack<?> itemInHand = connection.entity().inventory().itemInHand();
                        ((io.gomint.server.inventory.item.ItemStack<?>) itemInHand).calculateUsageAndUpdate(1);
                    }
                } else {
                    reset(packet, connection);
                }

                break;
            default:
                break;
        }
    }

    private void handleConsumeItem(
        ItemStack<?> itemInHand,
        PlayerConnection connection,
        PacketInventoryTransaction packet,
        ReleaseItemTransactionData trData
    ) {
        LOGGER.debug("Action Type: {}", trData.getActionType());
        if (trData.getActionType() == ReleaseItemTransactionData.ACTION_RELEASE) { // Release item ( shoot bow )
            // Check if the item is a bow
            if (itemInHand instanceof ItemBow) {
                ((ItemBow) itemInHand).shoot(connection.entity());
            } else if (itemInHand instanceof ItemCrossbow) {
                ((ItemCrossbow) itemInHand).shoot(connection.entity());
            }
        }

        connection.entity().setUsingItem(false);
    }

    private void handleUseItem(
        ItemStack<?> itemInHand,
        PlayerConnection connection,
        PacketInventoryTransaction packet,
        UseItemTransactionData trData
    ) {
        switch (trData.getActionType()) {
            case UseItemTransactionData.ACTION_CLICK_BLOCK: // Click on block
                // Only accept valid interactions
                if (!checkInteraction(packet, connection)) {
                    return;
                }

                connection.entity().setUsingItem(false);

                if (!connection.entity().world().useItemOn(itemInHand, trData.getBlockPosition(), trData.getFace(),
                    trData.getClickPosition(), connection.entity())) {
                    reset(packet, connection);
                    return;
                }

                break;
            case 1: // Click in air
                // Only accept valid interactions
                if (!checkInteraction(packet, connection)) {
                    return;
                }

                // Only send interact events, there is nothing special to be done in here
                PlayerInteractEvent event = new PlayerInteractEvent(connection.entity(), PlayerInteractEvent.ClickType.RIGHT, null);
                this.eventCaller.callEvent(event);

                if (!event.cancelled()) {
                    io.gomint.server.inventory.item.ItemStack<?> itemStack = (io.gomint.server.inventory.item.ItemStack<?>) connection.entity().inventory().itemInHand();
                    itemStack.interact(connection.entity(), null, trData.getClickPosition(), null);
                } else {
                    reset(packet, connection);
                }

                break;
            case 2: // Break block
                // Breaking blocks too fast / missing start_break
                if (connection.entity().gamemode() != Gamemode.CREATIVE && connection.entity().breakVector() == null) {
                    reset(packet, connection);
                    LOGGER.debug("Breaking block without break vector");
                    return;
                }

                if (connection.entity().gamemode() != Gamemode.CREATIVE) {
                    // Check for transactions first
                    if (packet.getActions().length > 1) {
                        // This can only have 0 or 1 transaction
                        reset(packet, connection);
                        connection.entity().breakVector(null);
                        return;
                    }

                    // The transaction can only affect the in hand item
                    if (packet.getActions().length > 0) {
                        ItemStack<?> source = packet.getActions()[0].getOldItem();
                        if (!source.equals(itemInHand) || source.amount() != itemInHand.amount()) {
                            // Transaction is invalid
                            reset(packet, connection);
                            connection.entity().breakVector(null);
                            return;
                        }

                        // Check if target item is either the same item or air
                        io.gomint.server.inventory.item.ItemStack<?> target = (io.gomint.server.inventory.item.ItemStack<?>) packet.getActions()[0].getNewItem();
                        if (!target.material().equals(((io.gomint.server.inventory.item.ItemStack<?>) itemInHand).material()) && !target.isAir()) {
                            // Transaction is invalid
                            reset(packet, connection);
                            connection.entity().breakVector(null);
                            return;
                        }
                    }
                }

                // Transaction seems valid
                io.gomint.server.world.block.Block block = connection.entity().world().blockAt(connection.entity().gamemode() == Gamemode.CREATIVE ? trData.getBlockPosition() : connection.entity().breakVector());
                if (block != null) {
                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(connection.entity(), block, connection.entity().gamemode() == Gamemode.CREATIVE ? new ArrayList<>() : block.drops(itemInHand));
                    blockBreakEvent.cancelled(connection.entity().gamemode() == Gamemode.ADVENTURE); // TODO: Better handling for canBreak rules for adventure gamemode

                    connection.entity().world().server().pluginManager().callEvent(blockBreakEvent);
                    if (blockBreakEvent.cancelled()) {
                        reset(packet, connection);
                        connection.entity().breakVector(null);
                        return;
                    }

                    // Check for special break rights (creative)
                    if (connection.entity().gamemode() == Gamemode.CREATIVE) {
                        if (connection.entity().world().breakBlock(trData.getBlockPosition(), blockBreakEvent.drops(), true)) {
                            block.blockType(BlockAir.class);
                            connection.entity().breakVector(null);
                        } else {
                            reset(packet, connection);
                            connection.entity().breakVector(null);
                        }

                        return;
                    }

                    // Check if we can break this block in time
                    long breakTime = block.finalBreakTime(connection.entity().inventory().itemInHand(), connection.entity());
                    if (breakTime > Values.CLIENT_TICK_MS && ((connection.entity().breakTime() + Values.CLIENT_TICK_MS) / (double) breakTime) < 0.75) {
                        LOGGER.warn(connection.entity().name() + " broke block too fast: break time: " + (connection.entity().breakTime() + Values.CLIENT_TICK_MS) +
                            "; should: " + breakTime + " for " + block.getClass().getSimpleName() + " with " + itemInHand.getClass().getSimpleName());
                        reset(packet, connection);
                        connection.entity().breakVector(null);
                    } else {
                        if (connection.entity().world().breakBlock(connection.entity().breakVector(), blockBreakEvent.drops(), false)) {
                            // Add exhaustion
                            connection.entity().exhaust(0.025f, PlayerExhaustEvent.Cause.MINING);

                            // Damage the target item
                            if (breakTime > Values.CLIENT_TICK_MS) {
                                int damage = 1;

                                // Swords get 2 calculateUsage
                                if (itemInHand instanceof ItemSword) {
                                    damage = 2;
                                }

                                io.gomint.server.inventory.item.ItemStack<?> itemStack = (io.gomint.server.inventory.item.ItemStack<?>) itemInHand;
                                itemStack.calculateUsageAndUpdate(damage);
                            }
                        } else {
                            reset(packet, connection);
                            return;
                        }

                        connection.entity().breakVector(null);
                    }
                } else {
                    reset(packet, connection);
                    connection.entity().breakVector(null);
                }

                break;
            default:
                break;
        }

    }

    private boolean checkInteraction(PacketInventoryTransaction packet, PlayerConnection connection) {
        // We cache all packets for this tick so we don't handle one twice, vanilla likes spam (https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Spam_can.png/220px-Spam_can.png)
        if (connection.transactionsHandled().contains(packet)) {
            return false;
        }

        connection.transactionsHandled().add(packet);
        return true;
    }

    private void handleTypeNormal(PlayerConnection connection, PacketInventoryTransaction packet) {
        connection.entity().setUsingItem(false);

        TransactionGroup transactionGroup = new TransactionGroup(connection.entity());
        for (NetworkTransaction transaction : packet.getActions()) {
            Inventory<?> inventory = getInventory(transaction, connection.entity());

            switch (transaction.getSourceType()) {
                case 99999:
                case 100:
                case 0:
                    // Normal inventory stuff
                    InventoryTransaction<?, ?, ?> inventoryTransaction = new InventoryTransaction<>(connection.entity(),
                        inventory, transaction.getSlot(), transaction.getOldItem(), transaction.getNewItem(), (byte) 0);
                    transactionGroup.addTransaction(inventoryTransaction);
                    break;
                case 2:
                    // Drop item
                    PlayerDropItemEvent playerDropItemEvent = new PlayerDropItemEvent(connection.entity(), transaction.getNewItem());
                    connection.server().pluginManager().callEvent(playerDropItemEvent);
                    if (!playerDropItemEvent.cancelled()) {
                        DropItemTransaction<?> dropItemTransaction = new DropItemTransaction<>(
                            connection.entity().location().add(0, connection.entity().eyeHeight(), 0),
                            connection.entity().direction().normalize().multiply(0.4f),
                            transaction.getNewItem());
                        transactionGroup.addTransaction(dropItemTransaction);
                    } else {
                        reset(packet, connection);
                    }

                    break;
                default:
                    break;
            }
        }

        transactionGroup.execute(connection.entity().gamemode() == Gamemode.CREATIVE);
    }

    private Inventory<?> getInventory(NetworkTransaction transaction, EntityPlayer entity) {
        Inventory<?> inventory = null;
        switch (transaction.getWindowId()) {
            case 0:     // EntityPlayer window id
                inventory = entity.inventory();
                break;
            case 119:
                inventory = entity.getOffhandInventory();
                break;
            case 120:   // Armor window id
                inventory = entity.armorInventory();
                break;
            case 124:   // Cursor window id
                if (transaction.getSlot() > 0) {
                    // WTF is slot 50?!
                    if (transaction.getSlot() == 50) {
                        transaction.setSlot(0);
                        inventory = entity.getCursorInventory();
                    } else {

                        // Crafting input
                        if (transaction.getSlot() >= 28 && transaction.getSlot() <= 31) {
                            inventory = entity.craftingInventory();
                            if (inventory.size() != 4) {
                                LOGGER.warn("Crafting inventory is not correctly sized");
                                return null;
                            }

                            transaction.setSlot(transaction.getSlot() - 28);
                        } else if (transaction.getSlot() >= 32 && transaction.getSlot() <= 40) {
                            inventory = entity.craftingInventory();
                            if (inventory.size() != 9) {
                                LOGGER.warn("Crafting inventory is not correctly sized");
                                return null;
                            }

                            transaction.setSlot(transaction.getSlot() - 32);
                        }
                    }
                } else {
                    inventory = entity.getCursorInventory();
                }
                break;

            default:
                // Check for container windows
                ContainerInventory<?> containerInventory = entity.getContainerId((byte) transaction.getWindowId());
                if (containerInventory != null) {
                    inventory = containerInventory;
                } else {
                    LOGGER.warn("Unknown window id: {}", transaction.getWindowId());
                }
        }

        return inventory;
    }

    private void reset(PacketInventoryTransaction packet, PlayerConnection connection) {
        // Reset whole inventory
        connection.entity().inventory().sendContents(connection);
        connection.entity().getOffhandInventory().sendContents(connection);
        connection.entity().armorInventory().sendContents(connection);

        // Does the viewer currently see any additional inventory?
        if (connection.entity().currentOpenContainer() != null) {
            connection.entity().currentOpenContainer().sendContents(connection);
        }

        // Now check if we need to reset blocks
        TransactionData trData = packet.getTrData();
        if (trData instanceof UseItemTransactionData) {
            this.reset0(connection, (UseItemTransactionData) trData);
        } else if (trData instanceof UseItemOnEntityTransactionData) {
            this.reset0(connection, (UseItemOnEntityTransactionData) trData);
        }

        // Resend attributes for consumptions
        connection.entity().resendAttributes();
    }

    private void reset0(PlayerConnection connection, UseItemTransactionData trData) {
        io.gomint.server.world.block.Block blockClicked = connection.entity().world().blockAt(trData.getBlockPosition());
        blockClicked.send(connection);

        if (trData.getFace() != null) {
            // Attach to block send queue
            io.gomint.server.world.block.Block replacedBlock = blockClicked.side(trData.getFace());
            replacedBlock.send(connection);

            for (Facing face : Facing.values()) {
                io.gomint.server.world.block.Block replacedSide = replacedBlock.side(face);
                replacedSide.send(connection);
            }
        }
    }


    private void reset0(PlayerConnection connection, UseItemOnEntityTransactionData trData) {
        io.gomint.server.world.block.Block blockClicked = connection.entity().world().blockAt(trData.getBlockPosition().toBlockPosition());
        blockClicked.send(connection);
    }
}
