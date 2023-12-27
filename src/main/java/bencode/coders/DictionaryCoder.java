package bencode.coders;

import bencode.Decode;
import bencode.coders.model.CodedResult;
import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;

import java.util.HashMap;
import java.util.Map;

public class DictionaryCoder implements Coder {
    @Override
    public CodedResult decode(String bencodedString) {

        Map<String, Object> dict = new HashMap<>();

        int currElementIndex = 1;
        boolean isKey = true;
        String key = "";
        System.out.print("{");
        while (bencodedString.charAt(currElementIndex) != 'e') {
            if (currElementIndex > 1 && isKey) {
                System.out.print(",");
            }
            CodedResult codedResult;


            if (isKey) {
                codedResult = Decode.decode(bencodedString.substring(currElementIndex));
                key = (String) codedResult.getResult();
                System.out.print(":");
                isKey = false;
            } else {
                codedResult = Decode.decode(bencodedString.substring(currElementIndex));
                dict.put(key, codedResult.getResult());
//                System.out.print(key+":"+codedResult.getResult());
                isKey = true;
            }
            currElementIndex += codedResult.getLength();

        }
        System.out.print("}");

        return CodedResult.builder().result(dict).length(currElementIndex + 1).build();
    }
}
