package bittorrent.bencode.handler.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

@Builder
@Setter
@Getter
public class TorrentStateModel {
    Map<String, Object> torrentMap;
    Map<String, Object> infoMap;
    String trackerUrl;
    byte[] infoHashRaw;
    String urlEncodedHash;
    List<InetSocketAddress> peers;
    private List<byte[]> pieces;

    public int getNumPieces() {
        if (pieces != null) return this.pieces.size();
        this.pieces = new ArrayList<>();
        byte[] pieces = (byte[]) infoMap.get("pieces");
        for (int i = 0; i < pieces.length; i += 20) {
            byte[] piece = new byte[20];
            System.arraycopy(pieces, i, piece, 0, 20);
            this.pieces.add(piece);
        }
        return this.pieces.size();
    }

    public Long getTotalLength() {
        return (Long) this.infoMap.get("length");
    }

    public byte[] getPieceHash(int pieceIdx) {
        if (pieces == null) getNumPieces();
        return this.pieces.get(pieceIdx);
    }

}
