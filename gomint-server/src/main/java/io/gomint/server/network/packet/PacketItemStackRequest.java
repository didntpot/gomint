/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.types.stackrequest.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketItemStackRequest extends Packet implements PacketServerbound {

    public static class Request {
        private int requestId;
        private List<InventoryAction> actions;

        public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {
            this.requestId = buffer.readUnsignedVarInt();

            int amountOfActions = buffer.readUnsignedVarInt();
            this.actions = new ArrayList<>(amountOfActions);
            for (int j = 0; j < amountOfActions; j++) {
                // Read type
                InventoryAction action;
                byte type = buffer.readByte();
                switch (type) {
                    case 0:
                        action = new InventoryMoveAction();
                        break;
                    case 1:
                        action = new InventoryPlaceAction();
                        break;
                    case 2:
                        action = new InventorySwapAction();
                        break;
                    case 3:
                        action = new InventoryDropAction();
                        break;
                    case 4:
                        action = new InventoryDestroyCreativeAction();
                        break;
                    case 5:
                        action = new InventoryConsumeAction();
                        break;
                    case 9:
                    case 10:
                        action = new InventoryCraftAction();
                        break;
                    case 11:
                        action = new InventoryGetCreativeAction();
                        break;
                    case 13:
                    case 14:
                        action = new InventoryCraftingResultAction();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + type);
                }

                action.deserialize(buffer, protocolID);
                this.actions.add(action);
            }

            int amountOfWords = buffer.readUnsignedVarInt();
            List<String> wordToFilter = new ArrayList<>(amountOfWords);
            for (int i = 0; i < amountOfWords; i++) {
                wordToFilter.add(buffer.readString());
            }
        }

        public int getRequestId() {
            return this.requestId;
        }

        public List<InventoryAction> getActions() {
            return this.actions;
        }

        @Override
        public String toString() {
            return "{\"_class\":\"Request\", " +
                "\"requestId\":\"" + this.requestId + "\"" + ", " +
                "\"actions\":" + (this.actions == null ? "null" : Arrays.toString(this.actions.toArray())) +
                "}";
        }
    }

    private List<Request> requests;

    /**
     * Construct a new packet
     */
    public PacketItemStackRequest() {
        super(Protocol.PACKET_ITEM_STACK_REQUEST);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) throws Exception {

    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {
        int amountOfRequests = buffer.readUnsignedVarInt();
        this.requests = new ArrayList<>(amountOfRequests);
        for (int i = 0; i < amountOfRequests; i++) {
            Request request = new Request();
            request.deserialize(buffer, protocolID);
            this.requests.add(request);
        }
    }

    public List<Request> getRequests() {
        return this.requests;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"PacketItemStackRequest\", " +
            "\"requests\":" + (this.requests == null ? "null" : Arrays.toString(this.requests.toArray())) +
            "}";
    }

}
