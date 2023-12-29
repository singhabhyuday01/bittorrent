package bittorrent.bencode.util;

import bittorrent.bencode.Decode;
import bittorrent.bencode.Encode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class TorrentUtil {
    public static String getInfoHashHex(Map<String, Object> infoMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Encode(baos).encode(infoMap);
        return HashUtil.shaHashHex(baos);
    }

    public static byte[] getInfoHashRaw(Map<String, Object> infoMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Encode(baos).encode(infoMap);
        return HashUtil.shaHashRaw(baos);
    }

    public static Map<String, Object> decodeTorrentFile(String filename) {
        File torrentFile = new File(filename);

        if (!torrentFile.exists()) {
            throw new RuntimeException("Torrent file does not exist");
        }

        try {
            FileInputStream fis = new FileInputStream(torrentFile);
            byte[] fileContent = fis.readAllBytes();
            fis.close();

            return (Map<String, Object>) new Decode(fileContent).decode();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
