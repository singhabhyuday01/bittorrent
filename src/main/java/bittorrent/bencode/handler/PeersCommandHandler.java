package bittorrent.bencode.handler;

import bittorrent.bencode.Decode;
import bittorrent.bencode.Print;
import bittorrent.bencode.util.TorrentUtil;
import okhttp3.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

public class PeersCommandHandler implements CommandHandler {
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

    private void handleResult(Object decodedObj) throws UnsupportedEncodingException {
        if (decodedObj instanceof Map<?, ?> resultMap) {
            Map<String, Object> infoMap = (Map<String, Object>) resultMap.get("info");

            String host = new String((byte[]) resultMap.get("announce"), StandardCharsets.ISO_8859_1);

            byte[] infoHashRaw = TorrentUtil.getInfoHashRaw(infoMap);
            String urlEncodedHash = URLEncoder.encode(new String(infoHashRaw, StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);

            Request request = new Request.Builder()
                    .get()
                    .url(
                            HttpUrl.parse(host)
                                    .newBuilder()
                                    .addEncodedQueryParameter("info_hash", urlEncodedHash)
                                    .addQueryParameter("peer_id", "00112233445566778899")
                                    .addQueryParameter("port", "6881")
                                    .addQueryParameter("uploaded", "0")
                                    .addQueryParameter("downloaded", "0")
                                    .addQueryParameter("left", String.valueOf(infoMap.get("length")))
                                    .addQueryParameter("compact", "1")
                                    .build()
                    )
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                InputStream inputStream = new ByteArrayInputStream(body.byteStream().readAllBytes());

                @SuppressWarnings("unchecked")
                Map<String, Object> decodedMap = (Map<String, Object>) new Decode(inputStream).decode();

                byte[] peers = (byte[]) decodedMap.get("peers");

                for (int i = 0; i < peers.length; i += 6) {
                    byte[] peer = new byte[4];
                    byte[] portBytes = new byte[2];
                    System.arraycopy(peers, i, peer, 0, 4);
                    System.arraycopy(peers, i + 4, portBytes, 0, 2);
                    int port = ((portBytes[0] & 0xFF) << 8) | (portBytes[1] & 0xFF);
                    InetAddress ia = InetAddress.getByAddress(peer);
                    InetSocketAddress isa = new InetSocketAddress(ia, port);
                    System.out.println(isa.getAddress().getHostAddress() + ":" + isa.getPort());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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
