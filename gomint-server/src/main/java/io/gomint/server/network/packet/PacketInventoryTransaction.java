package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.types.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketInventoryTransaction extends Packet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketInventoryTransaction.class);

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MISMATCH = 1;
    public static final int TYPE_USE_ITEM = 2;
    public static final int TYPE_USE_ITEM_ON_ENTITY = 3;
    public static final int TYPE_RELEASE_ITEM = 4;

    private int type;
    private NetworkTransaction[] actions;

    // New request id and changes slot (1.16)
    private int requestId;
    private ChangeSlot[] changeSlot;

    private TransactionData trData;

    /**
     * Construct a new packet
     */
    public PacketInventoryTransaction() {
        super(Protocol.PACKET_INVENTORY_TRANSACTION);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {

    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.requestId = buffer.readSignedVarInt();
        if (this.requestId != 0) {
            int length = buffer.readUnsignedVarInt();
            this.changeSlot = new ChangeSlot[length];
            for (int i = 0; i < length; i++) {
                this.changeSlot[i] = new ChangeSlot();
                this.changeSlot[i].deserialize(buffer);
            }
        }

        this.type = buffer.readUnsignedVarInt();

        // Read transaction action(s)
        int actionCount = buffer.readUnsignedVarInt();
        this.actions = new NetworkTransaction[actionCount];
        for (int i = 0; i < actionCount; i++) {
            NetworkTransaction networkTransaction = new NetworkTransaction();
            networkTransaction.read(buffer);
            this.actions[i] = networkTransaction;
        }

        // Read transaction data
        switch (this.type) {
            case TYPE_NORMAL:
                this.trData = new NormalTransactionData();
                break;
            case TYPE_MISMATCH:
                this.trData = new MismatchTransactionData();
                break;
            case TYPE_USE_ITEM:
                this.trData = new UseItemTransactionData();
                break;
            case TYPE_USE_ITEM_ON_ENTITY:
                this.trData = new UseItemOnEntityTransactionData();
                break;
            case TYPE_RELEASE_ITEM:
                this.trData = new ReleaseItemTransactionData();
                break;
        }
        this.trData.decode(buffer);
    }

    public static class ChangeSlot {
        private byte containerId;
        private byte[] changedSlots;

        /**
         * Deserialize a transaction action
         *
         * @param buffer Data from the packet
         */
        public void deserialize(PacketBuffer buffer) {
            this.containerId = buffer.readByte();

            int count = buffer.readUnsignedVarInt();
            this.changedSlots = new byte[count];
            buffer.readBytes(this.changedSlots);
        }

        public byte getContainerId() {
            return this.containerId;
        }

        public byte[] getChangedSlots() {
            return this.changedSlots;
        }
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NetworkTransaction[] getActions() {
        return this.actions;
    }

    public void setActions(NetworkTransaction[] actions) {
        this.actions = actions;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public ChangeSlot[] getChangeSlot() {
        return this.changeSlot;
    }

    public void setChangeSlot(ChangeSlot[] changeSlot) {
        this.changeSlot = changeSlot;
    }

    public TransactionData getTrData() {
        return this.trData;
    }

    public void setTrData(TransactionData trData) {
        this.trData = trData;
    }
}
