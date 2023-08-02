package io.gomint.server.network.packet.types;

import io.gomint.jraknet.PacketBuffer;

public class EducationUriResource {

    private String buttonName;
    private String linkUri;

    public EducationUriResource(String buttonName, String linkUri) {
        this.buttonName = buttonName;
        this.linkUri = linkUri;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeString(this.buttonName);
        buffer.writeString(this.linkUri);
    }

    public static EducationUriResource read(PacketBuffer buffer) {
        return new EducationUriResource(buffer.readString(), buffer.readString());
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public void setLinkUri(String linkUri) {
        this.linkUri = linkUri;
    }

    public String getButtonName() {
        return buttonName;
    }

    public String getLinkUri() {
        return linkUri;
    }
}
