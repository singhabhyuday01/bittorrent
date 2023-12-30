package bittorrent.bencode.handler.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
@Getter
public class PieceMessage implements Message {

    int index;
    int begin;
    byte[] block;

    @Override
    public int length() {
        return block.length + 1 + 8; // +1 for message id, +4 each for index, begin
    }

    @Override
    public void sendMessage(DataOutputStream outputStream) throws IOException {
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PIECE_MESSAGE;
    }
}
