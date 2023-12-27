package bencode.coders;

import bencode.coders.model.CodedResult;

public interface Coder {
    CodedResult decode(String bencodedString);
//    String encode(Object object);
}
