package bittorrent.bencode.handler.model;

import bittorrent.bencode.handler.model.messages.*;
import bittorrent.bencode.util.HashUtil;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.HexFormat;

@Getter
public class DownloadTorrentPiece extends Thread {
    String peerAddress;
    int peerPort;
    byte[] rawPeerId;
    String peerId;
    TorrentStateModel torrentStateModel;
    Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private final int pieceIdx;
    private byte[] pieceBytes;

    public DownloadTorrentPiece(String peerAddress, String peerPort, TorrentStateModel torrentStateModel, int pieceIdx) {
        this.peerAddress = peerAddress;
        this.peerPort = Integer.parseInt(peerPort);
        this.torrentStateModel = torrentStateModel;
        this.pieceIdx = pieceIdx;
    }

    @Override
    public void run() {
        performHandshake();
        getPiece();
    }

    public void performHandshake() {
        try {
            clientSocket = new Socket(peerAddress, peerPort);

            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            outputStream.write(19);
            outputStream.write("BitTorrent protocol".getBytes());
            outputStream.write(new byte[8]);
            outputStream.write(torrentStateModel.infoHashRaw);
            outputStream.write("00112233445566778899".getBytes());
            outputStream.flush();

            inputStream = new DataInputStream(clientSocket.getInputStream());
            byte[] handshakeResponse = new byte[68];
            inputStream.read(handshakeResponse);

            rawPeerId = new byte[20];
            System.arraycopy(handshakeResponse, 48, rawPeerId, 0, 20);

            peerId = HexFormat.of().formatHex(rawPeerId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getPiece() {
        try {

            final var bitfieldMessage = readMessage();
            if (!(bitfieldMessage instanceof BitfieldMessage)) {
                throw new RuntimeException("Expected bitfield message");
            }

            sendInterested();

            Long pieceLength = (Long) torrentStateModel.getInfoMap().get("piece length");
            // if last pieceIdx, then piece length will be less than pieceLength
            if (pieceIdx == torrentStateModel.getNumPieces() - 1) {
                pieceLength = torrentStateModel.getTotalLength() % pieceLength;
            }

            // send ceil(pieceLength/16384) Request messages
            double blockLength = 16384;
            int totalNumBlocks = (int) Math.ceil(pieceLength / blockLength);
            for (int blockIdx = 0; blockIdx < totalNumBlocks; blockIdx++) {
                int currBlockLength = (int) blockLength;
                if (blockIdx == totalNumBlocks - 1) {
                    currBlockLength = (int) (pieceLength % 16384);
                    if (currBlockLength == 0) currBlockLength = (int) blockLength;
                }
                sendMessage(new RequestMessage(pieceIdx, (int) (blockIdx * blockLength), currBlockLength));
            }

            // receive piece messages
            byte[] pieceBytes = new byte[pieceLength.intValue()];
            for (int blockIdx = 0; blockIdx < totalNumBlocks; blockIdx++) {
                Message message = readMessage();
                if (!(message instanceof PieceMessage pieceMessage)) {
                    blockIdx--;
                    continue;
                }
                System.arraycopy(pieceMessage.getBlock(), 0, pieceBytes, pieceMessage.getBegin(), pieceMessage.getBlock().length);
            }

            // compare with piece hash value
            byte[] receivedPieceHash = HashUtil.sha1(pieceBytes);
//            System.out.println(receivedPieceHash);
            byte[] originalPieceHash = torrentStateModel.getPieceHash(pieceIdx);
//            System.out.println(originalPieceHash);
            if (!Arrays.equals(receivedPieceHash, originalPieceHash)) {
                throw new RuntimeException("Piece hash does not match. Data integrity compromised.");
            }
            this.pieceBytes = pieceBytes;
            return pieceBytes;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void getAndSavePiece(String outputFileName) {
        byte[] pieceBytes = getPiece();
        writeToFile(outputFileName, pieceBytes);
        System.out.println("Piece " + pieceIdx + " downloaded to " + outputFileName);

    }

    @SneakyThrows
    private void writeToFile(String outputFileName, byte[] pieceBytes) {
        FileOutputStream fos = new FileOutputStream(outputFileName);
        fos.write(pieceBytes);
        fos.close();
    }

    @SneakyThrows
    private Message readMessage() {
        int payloadSize = getMessageLength() - 1;
        int messageId = getMessageId();

        MessageType messageType = MessageType.valueOf(messageId);

        return messageType.getDeserializer().deserialize(payloadSize, inputStream);
    }

    @SneakyThrows
    private void sendMessage(Message message) {
        message.sendMessage(outputStream);
    }

    @SneakyThrows
    private void sendInterested() {
        sendMessage(new InterestedMessage());
        while (true) {
            Message message = readMessage();
            if (message instanceof UnchokeMessage) {
                break;
            }
            System.err.println("Peer is choked");
            Thread.sleep(Duration.ofSeconds(1));
        }
    }

    @SneakyThrows
    private int getMessageLength() {
        return readIntFromStream(4);
    }

    @SneakyThrows
    private int getMessageId() {
        return readIntFromStream(1);
    }

    @SneakyThrows
    private int readIntFromStream(int numBytes) {
        byte[] bytes = new byte[numBytes];
        inputStream.read(bytes);
        int value = 0;
        for (byte b : bytes) {
            value = (value << 8) + (b & 0xFF);
        }
        return value;
    }

}
