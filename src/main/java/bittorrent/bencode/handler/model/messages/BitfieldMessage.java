package bittorrent.bencode.handler.model.messages;

import lombok.SneakyThrows;

import java.io.DataOutputStream;
import java.io.IOException;

public class BitfieldMessage implements Message {

    private final byte[] message;

    public BitfieldMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public int length() {
        return message.length + 1; // +1 for message id
    }

    @Override
    public void sendMessage(DataOutputStream outputStream) throws IOException {
        outputStream.write(length());
        outputStream.write(getMessageType().getMessageId());
        outputStream.write(message);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BITFIELD_MESSAGE;
    }
}
