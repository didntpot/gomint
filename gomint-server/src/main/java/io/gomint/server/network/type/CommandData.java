package io.gomint.server.network.type;

import io.gomint.server.network.packet.PacketAvailableCommands;
import java.util.List;
import javax.annotation.Nullable;

/**
 * @author geNAZt
 * @version 1.0
 */
public class CommandData {

    private final String name;
    private final String description;
    private byte flags;
    private byte permission;
    private @Nullable CommandEnum aliases;
    private List<CommandOverload> overloads;
    private List<ChainedSubCommandData> chainedSubCommandData;

    public CommandData(
        String name,
        String description,
        byte flags,
        byte permission,
        @Nullable CommandEnum aliases,
        List<CommandOverload> overloads,
        List<ChainedSubCommandData> chainedSubCommandData
    ) {
        this.name = name;
        this.description = description;
        this.flags = flags;
        this.permission = permission;
        this.aliases = aliases;
        this.overloads = overloads;
    }

    public void flags(byte flags) {
        this.flags = flags;
    }

    public void permission(byte permission) {
        this.permission = permission;
    }

    public void overloads(List<CommandOverload> overloads) {
        this.overloads = overloads;
    }

    public void chainedSubCommandData(List<ChainedSubCommandData> chainedSubCommandData) {
        this.chainedSubCommandData = chainedSubCommandData;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public byte flags() {
        return this.flags;
    }

    public byte permission() {
        return this.permission;
    }

    public @Nullable CommandEnum aliases() {
        return this.aliases;
    }

    public List<CommandOverload> overloads() {
        return this.overloads;
    }

    public List<ChainedSubCommandData> chainedSubCommandData() {
        return this.chainedSubCommandData;
    }

    public static class Parameter {
        private final String name;
        private final int type;
        private final boolean optional;
        private final int flags;
        private @Nullable CommandEnum commandEnum;
        private @Nullable String postfix;

        public Parameter(String name, int type, boolean optional, int flags, @Nullable CommandEnum commandEnum, @Nullable String postfix) {
            this.name = name;
            this.type = type;
            this.optional = optional;
            this.flags = flags;
            this.commandEnum = commandEnum;
            this.postfix = postfix;
        }

        public String name() {
            return this.name;
        }

        public int type() {
            return this.type;
        }

        public @Nullable CommandEnum commandEnum() {
            return this.commandEnum;
        }

        public @Nullable String postfix() {
            return this.postfix;
        }

        public boolean optional() {
            return this.optional;
        }

        public int flags() {
            return this.flags;
        }

        private static Parameter baseline(String name, int type, int flags, boolean optional) {
            return new Parameter(name, type, optional, flags, null, null);
        }

        public static Parameter standard(String name, int type, int flags, boolean optional) {
            return baseline(name, PacketAvailableCommands.ARG_FLAG_VALID | type, flags, optional);
        }

        public static Parameter standard(String nae, int type) {
            return standard(nae, type, 0, false);
        }

        public static Parameter postfixed(String name, String postfix, int flags, boolean optional) {
            Parameter result = baseline(name, PacketAvailableCommands.ARG_FLAG_POSTFIX, flags, optional);
            result.postfix = postfix;
            return result;
        }

        public static Parameter postfixed(String name, String postfix) {
            return postfixed(name, postfix, 0, false);
        }

        public static Parameter enumed(String name, CommandEnum commandEnum, int flags, boolean optional) {
            Parameter result = baseline(name, PacketAvailableCommands.ARG_FLAG_ENUM, flags, optional);
            result.commandEnum = commandEnum;
            return result;
        }

        public static Parameter enumed(String name, CommandEnum commandEnum) {
            return enumed(name, commandEnum, 0, false);
        }
    }

}
