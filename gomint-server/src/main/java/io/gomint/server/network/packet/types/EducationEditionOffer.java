package io.gomint.server.network.packet.types;

public class EducationEditionOffer {

    public static final int NONE = 0;
    public static final int EVERYWHERE_EXCEPT_CHINA = 1;
    public static final int CHINA = 2;

    private EducationEditionOffer() {
        throw new UnsupportedOperationException("Cannot construct EducationEditionOffer");
    }
}
