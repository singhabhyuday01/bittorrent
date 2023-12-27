package bittorrent.bencode;

import bittorrent.bencode.coders.DictionaryCoder;
import bittorrent.bencode.coders.IntegerCoder;
import bittorrent.bencode.coders.ListCoder;
import bittorrent.bencode.coders.StringCoder;
import bittorrent.bencode.coders.model.DecodedResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static bittorrent.bencode.util.InputStreamReaderUtil.peek;

public class Decode {

    InputStream inputStream;

    public Decode(String encodedString) {
        inputStream = new ByteArrayInputStream(encodedString.getBytes());
    }

    public Decode(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    public DecodedResult decode() {
        int first = peek(inputStream);

        if (Character.isDigit(first)) {
            // string decoding
            return new StringCoder(inputStream).decode();
        }
        if (first == 'i') {
            // integer decoding
            return new IntegerCoder(inputStream).decode();
        }
        if (first == 'l') {
            // list decoding
            return new ListCoder(inputStream).decode();
        }
        if (first == 'd') {
            // dictionary decoding
            return new DictionaryCoder(inputStream).decode();
        }
        throw new RuntimeException("Unsupported format");
    }
}
