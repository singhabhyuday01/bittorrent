package bittorrent.bencode.handler;

import bittorrent.bencode.handler.model.DownloadTorrentPiece;
import bittorrent.bencode.handler.model.TorrentStateModel;
import bittorrent.bencode.util.TorrentUtil;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.*;

public class DownloadCommandHandler implements CommandHandler {
    @Override
    public void handle(String[] args) {
        String outputFilename = args[2];
        String torrentFileName = args[3];

        handleResult(TorrentUtil.decodeTorrentFile(torrentFileName), outputFilename);

        System.out.println("Downloaded " + torrentFileName + " to " + outputFilename);
    }

    @SneakyThrows
    private void handleResult(Map<String, Object> resultMap, String outputFileName) {
        TorrentStateModel torrentStateModel = TorrentUtil.getTorrentInfo(resultMap);

        InetSocketAddress peer = torrentStateModel.getPeers().get(0);

        Map<Integer, DownloadTorrentPiece> pieceIdxToThread = new HashMap<>();

        for (int pieceIdx = 0; pieceIdx < torrentStateModel.getNumPieces(); pieceIdx++) {
            DownloadTorrentPiece t = new DownloadTorrentPiece(peer.getAddress().getHostAddress(), String.valueOf(peer.getPort()), torrentStateModel, pieceIdx);
            pieceIdxToThread.put(pieceIdx, t);
            t.performHandshake();
            t.getPiece();
            t.close();
        }

//        for (int pieceIdx = 0; pieceIdx < torrentStateModel.getNumPieces(); pieceIdx++) {
//            InetSocketAddress peer = torrentStateModel.getPeers().get(pieceIdx % torrentStateModel.getPeers().size());
//            DownloadTorrentPiece t = new DownloadTorrentPiece(peer.getAddress().getHostAddress(), String.valueOf(peer.getPort()), torrentStateModel, pieceIdx);
//            pieceIdxToThread.put(pieceIdx, t);
//            t.start();
//        }
//
//        for (Map.Entry<Integer, DownloadTorrentPiece> me : pieceIdxToThread.entrySet()) {
//            try {
//                me.getValue().join();
//                me.getValue().close();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // all pieces combined. write to a file
        FileOutputStream fos = new FileOutputStream(outputFileName);
        for (int i = 0; i < torrentStateModel.getNumPieces(); i++) {
            DownloadTorrentPiece piece = pieceIdxToThread.get(i);
            byte[] pieceBytes = piece.getPieceBytes();
            if (pieceBytes == null) {
                throw new RuntimeException("Problem downloading piece " + i + " from peer " + piece.getPeerAddress() + ":" + piece.getPeerPort() + ". Exiting.");
            }
            fos.write(pieceBytes);
            fos.flush();
        }

        fos.close();

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
