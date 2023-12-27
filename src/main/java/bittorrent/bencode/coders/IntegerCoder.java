package bittorrent.bencode.coders;

import bittorrent.bencode.coders.model.DecodedResult;

import java.io.InputStream;
import java.math.BigInteger;

import static bittorrent.bencode.util.InputStreamReaderUtil.poll;
import static bittorrent.bencode.util.InputStreamReaderUtil.readUntil;

public class IntegerCoder extends Coder {

    public IntegerCoder(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public DecodedResult decode() {
        poll(inputStream); // to read the first 'i'
        String integerString = readUntil(inputStream, 'e', false);
        BigInteger i = new BigInteger(integerString);
        result = DecodedResult.builder().result(i).coder(this).build();
        return result;
    }

    @Override
    public void printResult() {
        System.out.print(result.getResult());
    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
