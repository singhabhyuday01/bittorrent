package bittorrent.bencode.handler;

import bittorrent.bencode.handler.model.DownloadTorrent;
import bittorrent.bencode.handler.model.TorrentStateModel;
import bittorrent.bencode.util.TorrentUtil;

import java.net.InetSocketAddress;
import java.util.HexFormat;
import java.util.Map;

public class DownloadPieceCommandHandler implements CommandHandler {
    @Override
    public void handle(String[] args) {
        String outputFilename = args[2];
        String filename = args[3];
        int pieceNum = Integer.parseInt(args[4]);

        handleResult(TorrentUtil.decodeTorrentFile(filename), outputFilename, pieceNum);

    }

    private void handleResult(Map<String, Object> resultMap, String outputFileName, int pieceNum) {
        TorrentStateModel torrentStateModel = TorrentUtil.getTorrentInfo(resultMap);

        InetSocketAddress peer = torrentStateModel.getPeers().get(0);

//        torrentStateModel.getPeers().forEach(peer -> {
//        System.out.println("Connecting to peer: " + peer.getAddress().getHostAddress() + ":" + peer.getPort());
        DownloadTorrent handshakeState = new DownloadTorrent(peer.getAddress().getHostAddress(), String.valueOf(peer.getPort()), torrentStateModel);
        handshakeState.performHandshake();
//            System.out.println("Peer ID: " + handshakeState.getPeerId());

        handshakeState.getAndSavePiece(outputFileName, pieceNum);


        handshakeState.close();
//        });

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
