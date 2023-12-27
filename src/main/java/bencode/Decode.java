package bencode;

import bencode.coders.DictionaryCoder;
import bencode.coders.IntegerCoder;
import bencode.coders.ListCoder;
import bencode.coders.StringCoder;
import bencode.coders.model.CodedResult;

public class Decode {
    public static CodedResult decode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            // string decoding
            return new StringCoder().decode(bencodedString);
        }
        if (bencodedString.charAt(0) == 'i') {
            // integer decoding
            return new IntegerCoder().decode(bencodedString);
        }
        if (bencodedString.charAt(0) == 'l') {
            // list decoding
            return new ListCoder().decode(bencodedString);
        }
        if (bencodedString.charAt(0) == 'd') {
            // dictionary decoding
            return new DictionaryCoder().decode(bencodedString);
        }
        throw new RuntimeException("Unsupported format");
    }
}
