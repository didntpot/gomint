package io.gomint.server.network.packet.types.recipe;

public final class ItemDescriptorType {

    private ItemDescriptorType() {
        throw new UnsupportedOperationException("Cannot construct ItemDescriptorType");
    }

    public static final int INT_ID_META = 1;
    public static final int MOLANG = 2;
    public static final int TAG = 3;
    public static final int STRING_ID_META = 4;
    public static final int COMPLEX_ALIAS = 5;
}
