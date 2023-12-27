import code.Decode;
// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {

    public static void main(String[] args) throws Exception {

        String command = args[0];

        if ("decode".equals(command)) {
            String bencodedValue = args[1];
            try {
                Decode.decode(bencodedValue);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Unknown command: " + command);
        }
    }

}
