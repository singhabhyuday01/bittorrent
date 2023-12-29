package bittorrent.bencode.util;

import bittorrent.bencode.Encode;

import java.io.ByteArrayOutputStream;
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
}
