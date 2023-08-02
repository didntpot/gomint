package io.gomint.server.network.packet.types.transaction;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.packet.PacketInventoryTransaction;
import java.util.List;

public class NormalTransactionData extends TransactionData {

    @Override
    public int getTypeId() {
        return PacketInventoryTransaction.TYPE_NORMAL;
    }

    @Override
    public void decodeData(PacketBuffer buffer) {

    }

    @Override
    public void encodeData(PacketBuffer buffer) {

    }

    public static NormalTransactionData create(List<NetworkTransaction> transactions) {
        NormalTransactionData result = new NormalTransactionData();
        result.transactions = transactions;
        return result;
    }
}
