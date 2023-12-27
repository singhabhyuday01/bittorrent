package code.coders;

public class IntegerCoder implements Coder {
    @Override
    public Object decode(String bencodedString) {
        String integerString = bencodedString.substring(1, bencodedString.length() - 1);
        return Integer.parseInt(integerString);
    }

//    @Override
//    public String encode(Object object) {
//        return null;
//    }
}
