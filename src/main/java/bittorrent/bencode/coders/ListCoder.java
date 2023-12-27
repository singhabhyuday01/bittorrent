package bittorrent.bencode.coders;

import bittorrent.bencode.Decode;
import bittorrent.bencode.coders.model.DecodedResult;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static bittorrent.bencode.util.InputStreamReaderUtil.peek;
import static bittorrent.bencode.util.InputStreamReaderUtil.poll;

public class ListCoder extends Coder {

    public ListCoder(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public DecodedResult decode() {

        poll(inputStream); // to read first 'l'

        List<DecodedResult> resultList = new LinkedList<>();

        result = DecodedResult.builder().result(resultList).coder(this).build();

        while (peek(inputStream) != 'e') {
            DecodedResult codedResult = new Decode(inputStream).decode();
            resultList.add(codedResult);
        }
        poll(inputStream); // to read last 'e'
        return result;
    }

    @Override
    public void printResult() {
        System.out.print("[");
        List<DecodedResult> resultList = (List<DecodedResult>) result.getResult();

        int i=0;

        for (DecodedResult decodedResult : resultList) {
            if (i++ > 0) {
                System.out.print(",");
            }
            decodedResult.getCoder().printResult();
        }

        System.out.print("]");

    }
}
