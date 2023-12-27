import bencode.Decode;

import java.util.Arrays;
// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {

    public static void main(String[] args) throws Exception {
//        Object[] obj = {"hello", 1};
//        System.out.println(Arrays.toString(obj));
        String command = args[0];

        if ("decode".equals(command)) {
            String bencodedValue = args[1];
            try {
                Decode.decode(bencodedValue);
                System.out.println();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Unknown command: " + command);
        }
    }

}
