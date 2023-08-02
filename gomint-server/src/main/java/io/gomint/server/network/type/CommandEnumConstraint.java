package io.gomint.server.network.type;

import java.util.List;

@SuppressWarnings("PointlessBitwiseExpression")
public class CommandEnumConstraint {
    public static final int REQUIRES_CHEATS_ENABLED = 1 << 0;
    public static final int REQUIRES_ELEVATED_PERMISSIONS = 1 << 1;
    public static final int REQUIRES_HOST_PERMISSIONS = 1 << 2;
    public static final int REQUIRES_ALLOW_ALIASES = 1 << 3;

    private CommandEnum commandEnum;
    private int valueOffset;
    private List<Integer> constraints;

    public CommandEnumConstraint(
        CommandEnum commandEnum,
        int valueOffset,
        List<Integer> constraints
    ) {
        this.commandEnum = commandEnum;
        this.valueOffset = valueOffset;
        this.constraints = constraints;
    }

    public CommandEnum commandEnum() {
        return this.commandEnum;
    }

    public int valueOffset() {
        return this.valueOffset;
    }

    public List<Integer> constraints() {
        return this.constraints;
    }

    public String affectedValue() {
        return this.commandEnum.enumValues().get(this.valueOffset);
    }
}
