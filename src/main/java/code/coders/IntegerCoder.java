package code.coders;

import java.math.BigInteger;

public class IntegerCoder implements Coder {
    @Override
    public Object decode(String bencodedString) {
        String integerString = bencodedString.substring(1, bencodedString.length() - 1);
        BigInteger i = new BigInteger(integerString);
        System.out.println(i);
        return i;
    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
