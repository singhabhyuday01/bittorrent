package bittorrent.bencode;

import bittorrent.bencode.coders.DictionaryCoder;
import bittorrent.bencode.coders.NumericalCoder;
import bittorrent.bencode.coders.ListCoder;
import bittorrent.bencode.coders.StringCoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static bittorrent.bencode.util.InputStreamReaderUtil.peek;

public class Decode {

    InputStream inputStream;

    public Decode(String encodedString) {
        inputStream = new ByteArrayInputStream(encodedString.getBytes());
    }

    public Decode(byte[] bytes) {
        inputStream = new ByteArrayInputStream(bytes);
    }

    public Decode(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    public Object decode() {
        int first = peek(inputStream);

        if (Character.isDigit(first)) {
            // string decoding
            return new StringCoder().decode(inputStream);
        }
        if (first == 'i') {
            // integer decoding
            return new NumericalCoder().decode(inputStream);
        }
        if (first == 'l') {
            // list decoding
            return new ListCoder().decode(inputStream);
        }
        if (first == 'd') {
            // dictionary decoding
            return new DictionaryCoder().decode(inputStream);
        }
        throw new RuntimeException("Unsupported format");
    }
}
