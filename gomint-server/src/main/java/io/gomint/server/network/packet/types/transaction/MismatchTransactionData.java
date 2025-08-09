package io.gomint.server.network.packet.types.transaction;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.packet.PacketInventoryTransaction;
import io.gomint.server.network.packet.util.PacketDecodeException;

public class MismatchTransactionData extends TransactionData {

    @Override
    public int getTypeId() {
        return PacketInventoryTransaction.TYPE_MISMATCH;
    }

    @Override
    public void decodeData(PacketBuffer buffer) {
        if (!this.transactions.isEmpty()) {
            throw new PacketDecodeException("Mismatch transaction type should not have any actions associated with it, but got " + this.transactions.size() + " actions");
        }
    }

    @Override
    public void encodeData(PacketBuffer buffer) {

    }

    public static MismatchTransactionData create() {
        return new MismatchTransactionData();
    }
}
