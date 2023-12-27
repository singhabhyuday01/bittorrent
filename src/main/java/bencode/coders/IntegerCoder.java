package bencode.coders;

import bencode.coders.model.CodedResult;

import java.math.BigInteger;

public class IntegerCoder implements Coder {
    @Override
    public CodedResult decode(String bencodedString) {
        String integerString = bencodedString.substring(1, bencodedString.indexOf('e'));
        BigInteger i = new BigInteger(integerString);
        System.out.print(i);
        return CodedResult.builder().result(i).length(2 + integerString.length()).build();
    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
