package bittorrent.bencode.handler.model.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType {
    CHOKE_MESSAGE(0, (payloadSize, input) -> new ChokeMessage()),
    UNCHOKE_MESSAGE(1, (payloadSize, input) -> new UnchokeMessage()),
    INTERESTED_MESSAGE(2, (payloadSize, input) -> new InterestedMessage()),
    NOT_INTERESTED_MESSAGE(3, (payloadSize, input) -> new NotImplementedMessage()),
    HAVE_MESSAGE(4, (payloadSize, input) -> new NotImplementedMessage()),
    BITFIELD_MESSAGE(5, (payloadSize, input) -> new BitfieldMessage(input.readNBytes(payloadSize))),

    REQUEST_MESSAGE(6, (payloadSize, input) -> new RequestMessage(input.readInt(), input.readInt(), input.readInt())),
    PIECE_MESSAGE(7, (payloadSize, dataInputStream) -> new PieceMessage(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readNBytes(payloadSize - 8))),
    CANCEL_MESSAGE(8, (payloadSize, input) -> new NotImplementedMessage()),
    ;
    private final @Getter int messageId;
    private final @Getter MessageDeserializer deserializer;

    private static final MessageType[] VALUES = MessageType.values();

    public static MessageType valueOf(int messageId) {
        return VALUES[messageId];
    }

}
