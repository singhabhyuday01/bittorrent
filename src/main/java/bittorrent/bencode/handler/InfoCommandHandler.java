package bittorrent.bencode.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class InfoCommandHandler implements CommandHandler {
    @Override
    public void handle(String[] args) {
        String filename = args[1];

        File torrentFile = new File(filename);

        if (!torrentFile.exists()) {
            throw new RuntimeException("Torrent file does not exist");
        }

        try {
            FileInputStream fis = new FileInputStream(torrentFile);
            String fileContent = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(fileContent);
            fis.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }
}
