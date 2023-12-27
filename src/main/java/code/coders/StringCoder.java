package code.coders;

import com.google.gson.Gson;

public class StringCoder implements Coder {
    private static final Gson gson = new Gson();

    @Override
    public Object decode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            int firstColonIndex = 0;
            for (int i = 0; i < bencodedString.length(); i++) {
                if (bencodedString.charAt(i) == ':') {
                    firstColonIndex = i;
                    break;
                }
            }
            int length = Integer.parseInt(bencodedString.substring(0, firstColonIndex));
            String decodedString = bencodedString.substring(firstColonIndex + 1, firstColonIndex + 1 + length);
            System.out.println(gson.toJson(decodedString));
            return decodedString;
        } else {
            throw new RuntimeException("Only strings are supported at the moment");
        }
    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
