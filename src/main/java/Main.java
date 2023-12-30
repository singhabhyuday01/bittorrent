import bittorrent.bencode.handler.*;
// import com.dampcake.bittorrent.bencode.Bencode; - available if you need it!

public class Main {

    public static void main(String[] args) throws Exception {

        String command = args[0];

        if ("decode".equals(command)) {
            new DecodeCommandHandler().handle(args);
        } else if ("info".equals(command)) {
            new InfoCommandHandler().handle(args);
        } else if ("peers".equals(command)) {
            new PeersCommandHandler().handle(args);
        } else if ("handshake".equals(command)) {
            new HandshakeCommandHandler().handle(args);
        } else if ("download_piece".equals(command)) {
            new DownloadPieceCommandHandler().handle(args);
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

}
