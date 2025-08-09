package io.gomint.server.command;

import io.gomint.command.CommandOverload;
import io.gomint.command.ParamValidator;
import io.gomint.server.entity.EntityPlayer;
import io.gomint.server.network.packet.PacketAvailableCommands;
import io.gomint.server.network.type.CommandData;
import io.gomint.server.network.type.CommandEnum;
import io.gomint.server.util.collection.IndexedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author geNAZt
 * @version 1.0
 */
public class CommandPreprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandPreprocessor.class);

    // Enums are stored in an indexed list at the start. Enums are just collections of a name and
    // a integer list reflecting the index inside enumValues
    private List<String> enumValues = new ArrayList<>();
    private IndexedHashMap<String, List<Integer>> enums = new IndexedHashMap<>();
    private Map<CommandHolder, Integer> aliasIndex = new HashMap<>();
    private Map<String, Integer> enumIndexes = new HashMap<>();
    private List<String> postfixes = new ArrayList<>();

    // Cached commands packet
    private PacketAvailableCommands commandsPacket;

    /**
     * This preprocessor takes GoMint commands and merges them together into a PE format
     *
     * @param player   which should get the packet
     * @param commands which should be merged and written
     */
    public CommandPreprocessor(EntityPlayer player, List<CommandHolder> commands) {
        this.commandsPacket = new PacketAvailableCommands();

        // First we should scan all commands for aliases
        for (CommandHolder command : commands) {
            if (command.getAlias() != null) {
                for (String s : command.getAlias()) {
                    this.addEnum(command.getName() + "CommandAlias", s);
                }

                this.aliasIndex.put(command, this.enums.getIndex(command.getName() + "CommandAlias"));
            }
        }

        this.commandsPacket.setEnumValues(this.enumValues);

        // Now we need to search for enum validators
        for (CommandHolder command : commands) {
            if (command.getOverload() != null) {
                for (CommandOverload overload : command.getOverload()) {
                    if (overload.permission().isEmpty() || player.hasPermission(overload.permission())) {
                        if (overload.parameters() != null) {
                            for (Map.Entry<String, ParamValidator<?>> entry : overload.parameters().entrySet()) {
                                if (entry.getValue().hasValues()) {
                                    for (String s : entry.getValue().values()) {
                                        this.addEnum(command.getName() + "#" + entry.getKey(), s);
                                    }

                                    this.enumIndexes.put(command.getName() + "#" + entry.getKey(), this.enums.getIndex(command.getName() + "#" + entry.getKey()));
                                }

                                if (entry.getValue().postfix() != null && !this.postfixes.contains(entry.getValue().postfix())) {
                                    this.postfixes.add(entry.getValue().postfix());
                                }
                            }
                        }
                    }
                }
            }
        }

        this.commandsPacket.setEnums(this.enums);
        this.commandsPacket.setPostFixes(this.postfixes);

        // Now we should have sorted any enums. Move on to write the command data
        List<CommandData> commandDataList = new ArrayList<>();
        for (CommandHolder command : commands) {
            String lname = command.getName().toLowerCase();
            Set<String> aliases = command.getAlias();
            CommandEnum aliasObj = null;
            if (aliases != null && !aliases.isEmpty()) {
                if (!aliases.contains(lname)) {
                    aliases.add(lname);
                }
                aliasObj = new CommandEnum(command.getName() + "Aliases", Collections.list(Collections.enumeration(aliases)));
            }

            ArrayList<io.gomint.server.network.type.CommandOverload> o = new ArrayList<>();
            o.add(new io.gomint.server.network.type.CommandOverload(
                false,
                new ArrayList<>(
                    Collections.singletonList(
                        CommandData.Parameter.standard(
                            "args",
                            PacketAvailableCommands.ARG_TYPE_RAWTEXT,
                            0,
                            true
                        )
                    )
                )
            ));

            // Construct new data helper for the packet
            CommandData commandData = new CommandData(
                command.getName(),
                command.getDescription(),
                (byte) 0,
                (byte) 0,
                aliasObj,
                o,
                Collections.emptyList()
            );

            // Do we need to hack a bit here?
            // TODO: overload
//            List<List<CommandData.Parameter>> overloads = new ArrayList<>();
//
//            if (command.getOverload() != null) {
//                for (CommandOverload overload : command.getOverload()) {
//                    if (overload.permission().isEmpty() || player.hasPermission(overload.permission())) {
//                        List<CommandData.Parameter> parameters = new ArrayList<>();
//                        if (overload.parameters() != null) {
//                            for (Map.Entry<String, ParamValidator<?>> entry : overload.parameters().entrySet()) {
//                                // Build together type
//                                int paramType = 0; // We don't support postfixes yet
//
//                                switch (entry.getValue().type()) {
//                                    case INT:
//                                        if (entry.getValue().postfix() != null) {
//                                            paramType |= PacketAvailableCommands.ARG_FLAG_POSTFIX;
//                                            paramType |= this.postfixes.indexOf(entry.getValue().postfix());
//                                        } else {
//                                            paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                            paramType |= PacketAvailableCommands.ARG_TYPE_INT;
//                                        }
//
//                                        break;
//                                    case BOOL:
//                                    case STRING_ENUM:
//                                    case COMMAND:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_ENUM;
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= this.enumIndexes.get(command.getName() + "#" + entry.getKey());
//                                        break;
//                                    case TARGET:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= PacketAvailableCommands.ARG_TYPE_TARGET;
//                                        break;
//                                    case STRING:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= PacketAvailableCommands.ARG_TYPE_STRING;
//                                        break;
//                                    case BLOCK_POS:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= PacketAvailableCommands.ARG_TYPE_POSITION;
//                                        break;
//                                    case TEXT:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= PacketAvailableCommands.ARG_TYPE_RAWTEXT;
//                                        break;
//                                    case FLOAT:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= PacketAvailableCommands.ARG_TYPE_FLOAT;
//                                        break;
//                                    default:
//                                        paramType |= PacketAvailableCommands.ARG_FLAG_VALID;
//                                        paramType |= PacketAvailableCommands.ARG_TYPE_VALUE;
//                                }
//
//                                parameters.add(new CommandData.Parameter(entry.getKey(), paramType, entry.getValue().optional()));
//                            }
//                        }
//
//                        overloads.add(parameters);
//                    }
//                }
//            }
//
//            commandData.parameters(overloads);
            commandDataList.add(commandData);
        }

        this.commandsPacket.setCommandData(commandDataList);
    }

    private void addEnum(String name, String value) {
        // Check if we already know this enum value
        int enumValueIndex;
        if (!this.enumValues.contains(value)) {
            this.enumValues.add(value);
        }
        enumValueIndex = this.enumValues.indexOf(value);

        // Create / add this value to the enum
        List<Integer> old = this.enums.get(name);
        if (old == null) {    // DONT use computeIfAbsent, the index won't show up
            old = new ArrayList<>();
            this.enums.put(name, old);
        }

        old.add(enumValueIndex);
    }

    public PacketAvailableCommands getCommandsPacket() {
        return this.commandsPacket;
    }

}
