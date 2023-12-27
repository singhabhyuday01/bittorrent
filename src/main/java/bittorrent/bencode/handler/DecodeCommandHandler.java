package bittorrent.bencode.handler;

import bittorrent.bencode.Decode;
import bittorrent.bencode.coders.model.DecodedResult;

public class DecodeCommandHandler implements CommandHandler {
    @Override
    public void handle(String[] args) {
        try {
            String bencodedValue = args[1];
            DecodedResult cr = new Decode(bencodedValue).decode();
            cr.getCoder().printResult();
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
