package bittorrent.bencode.handler;

import bittorrent.bencode.Decode;
import bittorrent.bencode.Encode;
import bittorrent.bencode.util.HashUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

public class InfoCommandHandler implements CommandHandler {
    @Override
    public void handle(String[] args) {
        String filename = args[1];

        File torrentFile = new File(filename);

        if (!torrentFile.exists()) {
            throw new RuntimeException("Torrent file does not exist");
        }

        try {
            FileInputStream fis = new FileInputStream(torrentFile);
            byte[] fileContent = fis.readAllBytes();
            fis.close();

            Object o = new Decode(fileContent).decode();
            handleResult(o);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    private void handleResult(Object decodedObj) {
        if (decodedObj instanceof Map<?, ?> resultMap) {

            System.out.println("Tracker URL: " + new String((byte[]) resultMap.get("announce"), StandardCharsets.ISO_8859_1));

            Map<String, Object> infoMap = (Map<String, Object>) resultMap.get("info");

            System.out.println("Length: " + infoMap.get("length"));

            handleInfoHash(infoMap);
            handleInfoPieces(infoMap);
        }
    }

    private void handleInfoHash(Map<String, Object> infoMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String infoEncoded = new Encode(baos).encode(infoMap);
        System.out.println("Info Hash: " + HashUtil.shaHash(infoEncoded, baos));
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
