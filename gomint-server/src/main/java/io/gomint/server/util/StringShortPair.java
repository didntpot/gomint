/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.util;

/**
 * @author geNAZt
 * @version 1.0
 */
public class StringShortPair {

    private final String stringId;
    private final short numericId;

    public StringShortPair(String stringId, short numericId) {
        this.stringId = stringId;
        this.numericId = numericId;
    }

    public String getStringId() {
        return this.stringId;
    }

    public short getNumericId() {
        return this.numericId;
    }

    @Override
    public String toString() {
        return "StringShortPair{" +
            "stringId='" + this.stringId + '\'' +
            ", numericId=" + this.numericId +
            '}';
    }
}
