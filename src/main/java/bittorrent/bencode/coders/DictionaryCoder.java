package bittorrent.bencode.coders;

import bittorrent.bencode.Decode;
import bittorrent.bencode.coders.model.DecodedResult;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bittorrent.bencode.util.InputStreamReaderUtil.peek;
import static bittorrent.bencode.util.InputStreamReaderUtil.poll;

public class DictionaryCoder extends Coder {
    private static final Gson gson = new Gson();

    public DictionaryCoder(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public DecodedResult decode() {

        poll(inputStream); // to read first 'd'

        Map<String, DecodedResult> resultMap = new HashMap<>();

        result = DecodedResult.builder().result(resultMap).coder(this).build();

        boolean isKey = true;
        String key = "";

        while (peek(inputStream) != 'e') {

            DecodedResult codedResult = new Decode(inputStream).decode();

            if (isKey) {
                key = (String) codedResult.getResult();
                isKey = false;
            } else {
                resultMap.put(key, codedResult);
                isKey = true;
            }

        }
        poll(inputStream); // to read last 'e'

        return result;
    }

    @Override
    public void printResult() {
        System.out.print("{");
        Map<String, DecodedResult> resultMap = (Map<String, DecodedResult>) result.getResult();

        int i = 0;

        for (Map.Entry<String, DecodedResult> me : resultMap.entrySet()) {
            if (i++ > 0) {
                System.out.print(",");
            }
            System.out.print(gson.toJson(me.getKey()) + ":");
            me.getValue().getCoder().printResult();
        }

        System.out.print("}");

    }
}
