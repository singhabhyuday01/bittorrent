package bittorrent.bencode.coders;

import bittorrent.bencode.coders.model.DecodedResult;

import java.io.InputStream;

public abstract class Coder {

    InputStream inputStream;
    protected DecodedResult result = DecodedResult.builder().result("").build();

    public Coder(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public abstract DecodedResult decode();

    public abstract void printResult();
//    String encode(Object object);
}
