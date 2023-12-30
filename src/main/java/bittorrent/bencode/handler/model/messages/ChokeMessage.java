package bittorrent.bencode.handler.model.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class ChokeMessage implements Message {

    @Override
    public int length() {
        return 1; // +1 for message id
    }

    @Override
    public void sendMessage(DataOutputStream outputStream) throws IOException {

    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHOKE_MESSAGE;
    }
}
