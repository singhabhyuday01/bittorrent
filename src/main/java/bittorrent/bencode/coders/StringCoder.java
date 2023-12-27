package bittorrent.bencode.coders;

import bittorrent.bencode.coders.model.DecodedResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static bittorrent.bencode.util.InputStreamReaderUtil.readUntil;

public class StringCoder extends Coder {

    private static final Gson gson = new Gson();

    public StringCoder(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public DecodedResult decode() {

        String lengthString = readUntil(inputStream, ':', false);

        int length = Integer.parseInt(lengthString);
        try {
            String decodedString = new String(inputStream.readNBytes(length), StandardCharsets.US_ASCII);
            result = DecodedResult.builder().result(decodedString).coder(this).build();
            return result;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public void printResult() {
        System.out.print(gson.toJson(result.getResult()));
    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
