package bittorrent.bencode.handler;

import bittorrent.bencode.handler.model.DownloadTorrentPiece;
import bittorrent.bencode.handler.model.TorrentStateModel;
import bittorrent.bencode.util.TorrentUtil;

import java.util.HexFormat;
import java.util.Map;

public class HandshakeCommandHandler implements CommandHandler {
    @Override
    public void handle(String[] args) {
        String filename = args[1];
        String peerAddress = args[2];

        Map<String, Object> decodedMap = TorrentUtil.decodeTorrentFile(filename);
        handleResult(decodedMap, peerAddress);
    }

    private void handleResult(Map<String, Object> resultMap, String peerAddress) {
        Map<String, Object> infoMap = (Map<String, Object>) resultMap.get("info");

        byte[] infoHashRaw = TorrentUtil.getInfoHashRaw(infoMap);

        String[] peerAddressParts = peerAddress.split(":");

        DownloadTorrentPiece handshakeState = new DownloadTorrentPiece(peerAddressParts[0], peerAddressParts[1], TorrentStateModel.builder().infoHashRaw(infoHashRaw).build(), 0);
        handshakeState.performHandshake();
        System.out.println("Peer ID: " + handshakeState.getPeerId());
        handshakeState.close();

    }


    private void handleInfoPieces(Map<String, Object> infoMap) {
        System.out.println("Piece Length: " + infoMap.get("piece length"));
        byte[] pieces = (byte[]) infoMap.get("pieces");
        System.out.println("Piece Hashes:");
        for (int i = 0; i < pieces.length; i += 20) {
            byte[] piece = new byte[20];
            System.arraycopy(pieces, i, piece, 0, 20);

            System.out.println(HexFormat.of().formatHex(piece));
        }
    }

}
