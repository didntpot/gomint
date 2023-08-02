package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public abstract class RecipeWithTypeId {

    private int typeId;

    protected RecipeWithTypeId(int typeId) {
        this.typeId = typeId;
    }

    public final int getTypeId() {
        return this.typeId;
    }

    abstract public void encode(PacketBuffer buffer);
}
