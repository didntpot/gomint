package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

public class PacketSetTitle extends Packet implements PacketClientbound {

    private int type;
    private String text = "";
    private int fadeInTime = 0;
    private int stayTime = 0;
    private int fadeOutTime = 0;
    private String xuid = "";
    private String platformOnlineId = "";

    public PacketSetTitle() {
        super(Protocol.PACKET_SET_TITLE);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeSignedVarInt(this.type);
        buffer.writeString(this.text);
        buffer.writeSignedVarInt(this.fadeInTime);
        buffer.writeSignedVarInt(this.stayTime);
        buffer.writeSignedVarInt(this.fadeOutTime);
        buffer.writeString(this.xuid);
        buffer.writeString(this.platformOnlineId);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.type = buffer.readSignedVarInt();
        this.text = buffer.readString();
        this.fadeInTime = buffer.readSignedVarInt();
        this.stayTime = buffer.readSignedVarInt();
        this.fadeOutTime = buffer.readSignedVarInt();
        this.xuid = buffer.readString();
        this.platformOnlineId = buffer.readString();
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFadeInTime() {
        return this.fadeInTime;
    }

    public void setFadeInTime(int fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    public int getStayTime() {
        return this.stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getFadeOutTime() {
        return this.fadeOutTime;
    }

    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }

    public enum TitleType {

        TYPE_CLEAR(0),
        TYPE_RESET(1),
        TYPE_TITLE(2),
        TYPE_SUBTITLE(3),
        TYPE_ACTION_BAR(4),
        TYPE_ANIMATION_TIMES(5),
        TYPE_SET_TITLE_JSON(6),
        TYPE_SET_SUBTITLE_JSON(7),
        TYPE_SET_ACTIONBAR_MESSAGE_JSON(8);

        private final int id;

        TitleType(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

    }
}
