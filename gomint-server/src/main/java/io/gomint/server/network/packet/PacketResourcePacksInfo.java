package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.resource.ResourcePack;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketResourcePacksInfo extends Packet {

    private boolean mustAccept;
    private boolean hasScripts;
    private boolean forceServerPacks;
    private List<ResourcePack> behaviourPackEntries;
    private List<ResourcePack> resourcePackEntries;

    public PacketResourcePacksInfo() {
        super(Protocol.PACKET_RESOURCEPACK_INFO);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeBoolean(this.mustAccept);
        buffer.writeBoolean(this.hasScripts);
        buffer.writeBoolean(this.forceServerPacks);

        buffer.writeLShort((short) (this.behaviourPackEntries == null ? 0 : this.behaviourPackEntries.size()));
        if (this.behaviourPackEntries != null) {
            for (ResourcePack entry : this.behaviourPackEntries) {
                buffer.writeString(entry.version().id().toString());
                buffer.writeString(entry.version().version());
                buffer.writeLLong(entry.size());
                buffer.writeString("");
                buffer.writeString("");
                buffer.writeString("");
                buffer.writeBoolean(false);
            }
        }

        buffer.writeLShort((short) (this.resourcePackEntries == null ? 0 : this.resourcePackEntries.size()));
        if (this.resourcePackEntries != null) {
            for (ResourcePack entry : this.resourcePackEntries) {
                buffer.writeString(entry.version().id().toString()); // pack id
                buffer.writeString(entry.version().version()); // pack version
                buffer.writeLLong(entry.size()); // pack size in bytes
                buffer.writeString(""); // pack encryption key
                buffer.writeString(""); // pack subPack name
                buffer.writeString(""); // pack content Id
                buffer.writeBoolean(false); // has scripts
                buffer.writeBoolean(false); // is rtx capable
            }
        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.mustAccept = buffer.readBoolean();
        this.hasScripts = buffer.readBoolean();
        this.forceServerPacks = buffer.readBoolean();

        short behaviourAmount = buffer.readLShort();
        for (short i = 0; i < behaviourAmount; i++) {
            buffer.readString();
            buffer.readString();
            buffer.readLLong();
            buffer.readString();
            buffer.readString();
            buffer.readString();
            buffer.readBoolean();
        }

        behaviourAmount = buffer.readLShort();
        for (short i = 0; i < behaviourAmount; i++) {
            buffer.readString();
            buffer.readString();
            buffer.readLLong();
            buffer.readString();
            buffer.readString();
            buffer.readString();
            buffer.readBoolean();
        }
    }

    public boolean isMustAccept() {
        return this.mustAccept;
    }

    public void setMustAccept(boolean mustAccept) {
        this.mustAccept = mustAccept;
    }

    public boolean isHasScripts() {
        return this.hasScripts;
    }

    public void setHasScripts(boolean hasScripts) {
        this.hasScripts = hasScripts;
    }

    public List<ResourcePack> getBehaviourPackEntries() {
        return this.behaviourPackEntries;
    }

    public void setBehaviourPackEntries(List<ResourcePack> behaviourPackEntries) {
        this.behaviourPackEntries = behaviourPackEntries;
    }

    public List<ResourcePack> getResourcePackEntries() {
        return this.resourcePackEntries;
    }

    public void setResourcePackEntries(List<ResourcePack> resourcePackEntries) {
        this.resourcePackEntries = resourcePackEntries;
    }
}
