package bittorrent.bencode.handler;

import bittorrent.bencode.Decode;
import bittorrent.bencode.util.TorrentUtil;
import okhttp3.*;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

//        String host = new String((byte[]) resultMap.get("announce"), StandardCharsets.ISO_8859_1);

        byte[] infoHashRaw = TorrentUtil.getInfoHashRaw(infoMap);
        String urlEncodedHash = URLEncoder.encode(new String(infoHashRaw, StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);

//        Request request = new Request.Builder()
//                .get()
//                .url(
//                        HttpUrl.parse(host)
//                                .newBuilder()
//                                .addEncodedQueryParameter("info_hash", urlEncodedHash)
//                                .addQueryParameter("peer_id", "00112233445566778899")
//                                .addQueryParameter("port", "6881")
//                                .addQueryParameter("uploaded", "0")
//                                .addQueryParameter("downloaded", "0")
//                                .addQueryParameter("left", String.valueOf(infoMap.get("length")))
//                                .addQueryParameter("compact", "1")
//                                .build()
//                )
//                .build();


        String[] peerAddressParts = peerAddress.split(":");
        int peerPort = Integer.parseInt(peerAddressParts[1]);

        try {
            Socket clientSocket = new Socket(peerAddressParts[0], peerPort);

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(19);
            outputStream.write("BitTorrent protocol".getBytes());
            outputStream.write(new byte[8]);
            outputStream.write(infoHashRaw);
            outputStream.write("00112233445566778899".getBytes());
            outputStream.flush();

            InputStream inputStream = clientSocket.getInputStream();
            byte[] handshakeResponse = new byte[68];
            inputStream.read(handshakeResponse);

            byte[] peerId = new byte[20];
            System.arraycopy(handshakeResponse, 48, peerId, 0, 20);

            System.out.println("Peer ID: "+HexFormat.of().formatHex(peerId));

            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
