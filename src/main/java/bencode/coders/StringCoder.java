package bencode.coders;

import bencode.coders.model.CodedResult;
import com.google.gson.Gson;

public class StringCoder implements Coder {
    private static final Gson gson = new Gson();

    @Override
    public CodedResult decode(String bencodedString) {

        int firstColonIndex = 0;
        for (int i = 0; i < bencodedString.length(); i++) {
            if (bencodedString.charAt(i) == ':') {
                firstColonIndex = i;
                break;
            }
        }
        int length = Integer.parseInt(bencodedString.substring(0, firstColonIndex));
        String decodedString = bencodedString.substring(firstColonIndex + 1, firstColonIndex + 1 + length);
        System.out.print(gson.toJson(decodedString));
        return CodedResult.builder().result(decodedString).length(firstColonIndex + 1 + length).build();

    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
