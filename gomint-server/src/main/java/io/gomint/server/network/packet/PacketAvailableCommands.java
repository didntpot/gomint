package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.type.*;
import io.gomint.server.util.collection.IndexedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketAvailableCommands extends Packet implements PacketClientbound {

    public static final int ARG_FLAG_VALID = 0x100000;

    public static final int ARG_TYPE_INT = 1;
    public static final int ARG_TYPE_FLOAT = 3;
    public static final int ARG_TYPE_VALUE = 4;
    public static final int ARG_TYPE_WILDCARD_INT = 5;
    public static final int ARG_TYPE_OPERATOR = 6;
    public static final int ARG_TYPE_COMPARE_OPERATOR = 7;
    public static final int ARG_TYPE_TARGET = 8;

    public static final int ARG_TYPE_WILDCARD_TARGET = 10;

    public static final int ARG_TYPE_FILEPATH = 17;

    public static final int ARG_TYPE_FULL_INTEGER_RANGE = 23;

    public static final int ARG_TYPE_EQUIPMENT_SLOT = 43;
    public static final int ARG_TYPE_STRING = 44;

    public static final int ARG_TYPE_INT_POSITION = 52;
    public static final int ARG_TYPE_POSITION = 53;

    public static final int ARG_TYPE_MESSAGE = 55;

    public static final int ARG_TYPE_RAWTEXT = 58;

    public static final int ARG_TYPE_JSON = 62;

    public static final int ARG_TYPE_BLOCK_STATES = 71;

    public static final int ARG_TYPE_COMMAND = 74;

    /**
     * Enums are a little different: they are composed as follows:
     * ARG_FLAG_ENUM | ARG_FLAG_VALID | (enum index)
     */
    public static final int ARG_FLAG_ENUM = 0x200000;

    /**
     * This is used for /xp <level: int>L. It can only be applied to integer parameters.
     */
    public static final int ARG_FLAG_POSTFIX = 0x1000000;

    public static final int ARG_FLAG_SOFT_ENUM = 0x4000000;

    public static final Map<String, Boolean> HARDCODED_ENUM_NAMES = new HashMap<>();

    private List<String> enumValues;
    private List<String> postFixes;
    private IndexedHashMap<String, List<Integer>> enums;
    private List<CommandData> commandData;
    private List<CommandEnum> hardcodedEnums;
    private List<CommandEnum> softEnums;
    private List<CommandEnumConstraint> enumConstraints;

    static {
        HARDCODED_ENUM_NAMES.put("CommandName", true);
    }

    /**
     * Construct a new packet
     */
    public PacketAvailableCommands() {
        super(Protocol.PACKET_AVAILABLE_COMMANDS);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        Map<String, Integer> enumValueIndexes = new HashMap<>();
        Map<String, Integer> postfixIndexes = new HashMap<>();
        Map<String, CommandEnum> enums = new HashMap<>();
        Map<String, Integer> enumIndexes = new HashMap<>();
        Map<String, CommandEnum> softEnums = new HashMap<>();
        Map<String, Integer> softEnumIndexes = new HashMap<>();
        Map<String, ChainedSubCommandData> allChainedSubCommandData = new HashMap<>();
        Map<String, Integer> chainedSubCommandDataIndexes = new HashMap<>();
        for (CommandEnum commandEnum : hardcodedEnums) {
            this.addEnumFn(commandEnum, enums, softEnums, enumIndexes, softEnumIndexes, enumValueIndexes);
        }
        for (CommandEnum commandEnum : this.softEnums) {
            this.addEnumFn(commandEnum, enums, softEnums, enumIndexes, softEnumIndexes, enumValueIndexes);
        }
        for (CommandData commandData : this.commandData) {
            if (commandData.aliases() != null) {
                this.addEnumFn(commandData.aliases(), enums, softEnums, enumIndexes, softEnumIndexes, enumValueIndexes);
            }
            for (CommandOverload overload : commandData.overloads()) {
                for (CommandData.Parameter parameter : overload.parameters()) {
                    if (parameter.commandEnum() != null) {
                        this.addEnumFn(parameter.commandEnum(), enums, softEnums, enumIndexes, softEnumIndexes, enumValueIndexes);
                    }

                    if (parameter.postfix() != null) {
                        postfixIndexes.put(parameter.postfix(), postfixIndexes.getOrDefault(parameter.postfix(), postfixIndexes.size()));
                    }
                }
            }
            for (ChainedSubCommandData chainedSubCommandData : commandData.chainedSubCommandData()) {
                if (!allChainedSubCommandData.containsKey(chainedSubCommandData.name())) {
                    allChainedSubCommandData.put(chainedSubCommandData.name(), chainedSubCommandData);
                    chainedSubCommandDataIndexes.put(chainedSubCommandData.name(), chainedSubCommandDataIndexes.size());

                    for (ChainedSubCommandData.ChainedSubCommandValue value : chainedSubCommandData.values()) {
                        chainedSubCommandDataIndexes.put(value.name(), chainedSubCommandDataIndexes.size());
                    }
                }
            }
        }

        buffer.writeUnsignedVarInt(enumValueIndexes.size());
        for (String enumValue : enumValueIndexes.keySet()) {
            buffer.writeString(enumValue);
        }

        buffer.writeUnsignedVarInt(chainedSubCommandDataIndexes.size());
        for (String chainedSubCommandValueName : chainedSubCommandDataIndexes.keySet()) {
            buffer.writeString(chainedSubCommandValueName);
        }

        buffer.writeUnsignedVarInt(postfixIndexes.size());
        for (String postfix : postfixIndexes.keySet()) {
            buffer.writeString(postfix);
        }

        buffer.writeUnsignedVarInt(enums.size());
        for (CommandEnum commandEnum : enums.values()) {
            this.putEnum(commandEnum, enumValueIndexes, buffer);
        }

        buffer.writeUnsignedVarInt(allChainedSubCommandData.size());
        for (ChainedSubCommandData chainedSubCommandData : allChainedSubCommandData.values()) {
            buffer.writeString(chainedSubCommandData.name());
            buffer.writeUnsignedVarInt(chainedSubCommandData.values().size());
            for (ChainedSubCommandData.ChainedSubCommandValue value : chainedSubCommandData.values()) {
                buffer.writeLShort(chainedSubCommandDataIndexes.get(value.name()).shortValue());
                buffer.writeLShort((short) value.type());
            }
        }

        buffer.writeUnsignedVarInt(this.commandData.size());
        for (CommandData data : this.commandData) {
            this.putCommandData(data, enumIndexes, softEnumIndexes, postfixIndexes, chainedSubCommandDataIndexes, buffer);
        }

        buffer.writeUnsignedVarInt(softEnums.size());
        for (CommandEnum commandEnum : softEnums.values()) {
            this.putSoftEnum(commandEnum, buffer);
        }

        buffer.writeUnsignedVarInt(this.enumConstraints.size());
        for (CommandEnumConstraint constraint : this.enumConstraints) {
            this.putEnumConstraint(constraint, enumIndexes, enumValueIndexes, buffer);
        }
    }

    protected void putEnum(CommandEnum commandEnum, Map<String, Integer> enumValueMap, PacketBuffer buffer) {
        buffer.writeString(commandEnum.enumName());

        List<String> values = commandEnum.enumValues();
        buffer.writeUnsignedVarInt(values.size());
        int listSize = enumValueMap.size();
        for (String value : values) {
            this.putEnumValueIndex(enumValueMap.get(value), listSize, buffer);
        }
    }

    protected void putSoftEnum(CommandEnum commandEnum, PacketBuffer buffer) {
        buffer.writeString(commandEnum.enumName());

        List<String> values = commandEnum.enumValues();
        buffer.writeUnsignedVarInt(values.size());
        for (String value : values) {
            buffer.writeString(value);
        }
    }

    protected void putEnumValueIndex(int index, int valueCount, PacketBuffer buffer) {
        if (valueCount < 256) {
            buffer.writeByte((byte) index);
        } else if (valueCount < 65535) {
            buffer.writeLShort((short) index);
        } else {
            buffer.writeLInt(index);
        }
    }

    protected void putEnumConstraint(
        CommandEnumConstraint constraint,
        Map<String, Integer> enumIndexes,
        Map<String, Integer> enumValueIndexes,
        PacketBuffer buffer
    ) {
        buffer.writeLInt(enumValueIndexes.get(constraint.affectedValue()));
        buffer.writeLInt(enumIndexes.get(constraint.commandEnum().enumName()));
        buffer.writeUnsignedVarInt(constraint.constraints().size());
        for (int constraintValue : constraint.constraints()) {
            buffer.writeByte((byte) constraintValue);
        }
    }

    protected void putCommandData(
        CommandData data,
        Map<String, Integer> enumIndexes,
        Map<String, Integer> softEnumIndexes,
        Map<String, Integer> postfixIndexes,
        Map<String, Integer> chainedSubCommandDataIndexes,
        PacketBuffer buffer
    ) {
        buffer.writeString(data.name());
        buffer.writeString(data.description());
        buffer.writeLShort(data.flags());
        buffer.writeByte(data.permission());

        if (data.aliases() != null) {
            buffer.writeLInt(enumIndexes.getOrDefault(data.aliases().enumName(), -1));
        } else {
            buffer.writeLInt(-1);
        }

        buffer.writeUnsignedVarInt(data.chainedSubCommandData().size());
        for (ChainedSubCommandData chainedSubCommandData : data.chainedSubCommandData()) {
            buffer.writeLShort(chainedSubCommandDataIndexes.get(chainedSubCommandData.name()).shortValue());
        }

        buffer.writeUnsignedVarInt(data.overloads().size());
        for (CommandOverload overload : data.overloads()) {
            buffer.writeBoolean(overload.chaining());
            buffer.writeUnsignedVarInt(overload.parameters().size());
            for (CommandData.Parameter parameter : overload.parameters()) {
                buffer.writeString(parameter.name());

                if (parameter.commandEnum() != null) {
                    if (parameter.commandEnum().isSoft()) {
                        buffer.writeLInt(ARG_FLAG_SOFT_ENUM | ARG_FLAG_ENUM | softEnumIndexes.get(parameter.commandEnum().enumName()));
                    } else {
                        buffer.writeLInt(ARG_FLAG_ENUM | enumIndexes.get(parameter.commandEnum().enumName()));
                    }
                } else if (parameter.postfix() != null) {
                    buffer.writeLInt(ARG_FLAG_POSTFIX | postfixIndexes.get(parameter.postfix()));
                } else {
                    buffer.writeLInt(parameter.type());
                }

                buffer.writeBoolean(parameter.optional());
                buffer.writeByte((byte) 0);
            }
        }
    }

    private void addEnumFn(
        CommandEnum commandEnum,
        Map<String, CommandEnum> enums,
        Map<String, CommandEnum> softEnums,
        Map<String, Integer> enumIndexes,
        Map<String, Integer> softEnumIndexes,
        Map<String, Integer> enumValueIndexes
    ) {
        String enumName = commandEnum.enumName();
        if (commandEnum.isSoft()) {
            if (!softEnumIndexes.containsKey(enumName)) {
                softEnumIndexes.put(enumName, softEnums.size());
                softEnums.put(enumName, commandEnum);
            }
        } else {
            for (String value : commandEnum.enumValues()) {
                enumValueIndexes.put(value, enumValueIndexes.getOrDefault(value, enumValueIndexes.size()));
            }
            if (!enumIndexes.containsKey(enumName)) {
                enumIndexes.put(enumName, enums.size());
                enums.put(enumName, commandEnum);
            }
        }

    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {

    }

    public List<String> getEnumValues() {
        return this.enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    public List<String> getPostFixes() {
        return this.postFixes;
    }

    public void setPostFixes(List<String> postFixes) {
        this.postFixes = postFixes;
    }

    public IndexedHashMap<String, List<Integer>> getEnums() {
        return this.enums;
    }

    public void setEnums(IndexedHashMap<String, List<Integer>> enums) {
        this.enums = enums;
    }

    public List<CommandData> getCommandData() {
        return this.commandData;
    }

    public void setCommandData(List<CommandData> commandData) {
        this.commandData = commandData;
    }
}
