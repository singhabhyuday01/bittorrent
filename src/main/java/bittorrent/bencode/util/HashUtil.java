package bittorrent.bencode.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HashUtil {
    public static String shaHash(String input, ByteArrayOutputStream outputStream) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-1").digest(
                    outputStream.toByteArray());
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
