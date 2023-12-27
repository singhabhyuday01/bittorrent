package bencode.coders;

import bencode.Decode;
import bencode.coders.model.CodedResult;

import java.util.LinkedList;
import java.util.List;

public class ListCoder implements Coder {
    @Override
    public CodedResult decode(String bencodedString) {

        List<Object> results = new LinkedList<>();
        System.out.print("[");

        int currElementIndex = 1;
        while (bencodedString.charAt(currElementIndex) != 'e') {
            if (currElementIndex > 1) {
                System.out.print(",");
            }
            CodedResult codedResult = Decode.decode(bencodedString.substring(currElementIndex));
            results.add(codedResult.getResult());
            currElementIndex += codedResult.getLength();
        }
        System.out.print("]");

        return CodedResult.builder().result(results).length(currElementIndex + 1).build();
    }
}
