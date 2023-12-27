package code;

import code.coders.IntegerCoder;
import code.coders.StringCoder;

public class Decode {
    public static Object decode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            // string decoding
            return new StringCoder().decode(bencodedString);
        }
        if (bencodedString.charAt(0) == 'i') {
            // integer decoding
            return new IntegerCoder().decode(bencodedString);
        }
        throw new RuntimeException("Unsupported format");
    }
}
