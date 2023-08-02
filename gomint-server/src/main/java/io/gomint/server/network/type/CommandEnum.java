package io.gomint.server.network.type;

import java.util.List;

public class CommandEnum {

    private String enumName;
    private List<String> enumValues;
    private Boolean isSoft;

    public CommandEnum(
        String enumName,
        List<String> enumValues,
        Boolean isSoft
    ) {
        this.enumName = enumName;
        this.enumValues = enumValues;
        this.isSoft = isSoft;
    }

    public CommandEnum(
        String enumName,
        List<String> enumValues
    ) {
        this(enumName, enumValues, false);
    }

    public String enumName() {
        return this.enumName;
    }

    public List<String> enumValues() {
        return this.enumValues;
    }

    public Boolean isSoft() {
        return this.isSoft;
    }
}
