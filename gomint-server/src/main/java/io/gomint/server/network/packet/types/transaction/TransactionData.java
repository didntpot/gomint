package io.gomint.server.network.packet.types.transaction;

import io.gomint.jraknet.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public abstract class TransactionData {

    protected List<NetworkTransaction> transactions = new ArrayList<>();

    abstract public int getTypeId();

    final public void decode(PacketBuffer buffer) {
        int actionCount = buffer.readUnsignedVarInt();
        for (int i = 0; i < actionCount; ++i) {
            this.transactions.add(new NetworkTransaction().read(buffer));
        }
        this.decodeData(buffer);
    }

    abstract public void decodeData(PacketBuffer buffer);

    final public void encode(PacketBuffer buffer) {
        buffer.writeUnsignedVarInt(this.transactions.size());
        for (NetworkTransaction transaction : this.transactions) {
            transaction.write(buffer);
        }
        this.encodeData(buffer);
    }

    abstract public void encodeData(PacketBuffer buffer);

    public List<NetworkTransaction> getTransactions() {
        return this.transactions;
    }
}
