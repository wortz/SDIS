package channels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import peer.Peer;
import utility.Utility;
import file.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.io.InputStreamReader;

public class MessageHandler implements Runnable {

    protected byte[] packet;

    public MessageHandler(byte[] packet) {
        this.packet = packet;
    }

    public void run() {
        String header = getHeaderPacket();
        String[] headerSplit = header.split(" ");
        if (headerSplit[2].equals(Peer.getId()))
            return;
        switch (headerSplit[0]) {
        case "PUTCHUNK":
            managePutchunk(headerSplit, header.length());
            break;
        case "STORED":
            manageStored(headerSplit);
            break;
        case "GETCHUNK":
            manageGetChunk(headerSplit);
            break;
        case "CHUNK":
            manageChunk(headerSplit, header.length());
            break;
        case "DELETE":
            manageDelete(headerSplit);
            break;
        case "REMOVED":
            manageRemoved();
            break;

        }
    }

    private synchronized void managePutchunk(String[] headerSplit, int headerLength) {
        // PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg>
        // <CRLF><CRLF><Body>
        double version = Double.parseDouble(headerSplit[1]);
        String senderID = headerSplit[2];
        String fileId = headerSplit[3];
        int chunkNr = Integer.parseInt(headerSplit[4]);
        int repDegree = Integer.parseInt(headerSplit[5]);

        byte[] body = new byte[(this.packet.length - headerLength - 4)];
        System.arraycopy(this.packet, headerLength + 4, body, 0, body.length);

        Chunk chunk = new Chunk(chunkNr, body, repDegree, fileId, senderID);
        //adds to the processing chunks
        Peer.getStorage().addProcessingChunk(chunk);
        try {
            String storedResponse = "STORED " + headerSplit[1] + " " + Peer.getId() + " " + fileId + " " + chunkNr
                    + Utility.CRLF + Utility.CRLF;
            byte[] stored = storedResponse.getBytes("US-ASCII");
            Thread.sleep(Utility.getRandomValue(Utility.MAX_WAIT_TIME));
            if (!Peer.getStorage().finishedDegree(chunkNr, fileId) || version==1.0){
                System.out.println("Stored : " + chunkNr);
                Peer.getMC().message(stored);
                String pathBackup = "../PeerStorage/peer" + Peer.getId() + "/" + "backup";
                File backupDir = new File(pathBackup);
                if (!backupDir.exists()) {
                    backupDir.mkdirs();
                }
                String pathFileId = pathBackup + "/" + fileId;
                File pathToFile = new File(pathFileId);
                if (!pathToFile.exists()) {
                    pathToFile.mkdir();
                }

                String chunkDir = pathToFile + "/" + "chunk" + chunkNr;
                File chunkFile = new File(chunkDir);
                if (!chunkFile.exists()) {
                    FileOutputStream fos = new FileOutputStream(chunkDir);
                    fos.write(body);
                }
                Peer.getStorage().updateProcessingChunk(chunk);
            } else {
                Peer.getStorage().removeProcessingChunk(chunk);
            }

        } catch (IOException e) {
            System.out.println("Error sending Stored message.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void manageStored(String[] headerSplit) {
        // STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        System.out.println("Received stored " + headerSplit[4]);
        Peer.getStorage().incRepDegree(Integer.parseInt(headerSplit[4]), headerSplit[3], headerSplit[2]);
    }

    private synchronized void manageGetChunk(String[] headerSplit) {
        // GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        double version = Double.parseDouble(headerSplit[1]);
        String senderID = headerSplit[2];
        String fileId = headerSplit[3];
        int chunkNr = Integer.parseInt(headerSplit[4]);
        try {

            for (Chunk chunk : Peer.getStorage().getChunksStored()) {
                if (chunk.compareChunk(chunkNr, fileId)) {
                    byte[] body = chunk.getData();

                    String headerMessage = "CHUNK " + version + " " + Peer.getId() + " " + fileId + " " + chunkNr
                            + Utility.CRLF + Utility.CRLF;
                    byte[] header = headerMessage.getBytes("US-ASCII");
                    byte[] message = new byte[header.length + body.length];
                    System.arraycopy(header, 0, message, 0, header.length);
                    System.arraycopy(body, 0, message, header.length, body.length);
                    Thread.sleep(Utility.getRandomValue(Utility.MAX_WAIT_TIME));
                    if (!Peer.getStorage().alreadyRestored(chunk))
                        System.out.println("Sending chunk message");
                        Peer.getMDR().message(message);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private synchronized void manageChunk(String[] headerSplit, int headerLength) {
        // CHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
        double version = Double.parseDouble(headerSplit[1]);
        String senderID = headerSplit[2];
        String fileId = headerSplit[3];
        int chunkNr = Integer.parseInt(headerSplit[4]);

        byte[] body = new byte[(this.packet.length - headerLength - 4)];
        System.arraycopy(this.packet, headerLength + 4, body, 0, body.length);

        Chunk chunk = new Chunk(chunkNr, body, 1, fileId, senderID);
        Peer.getStorage().addRestored(chunk);

    }

    private synchronized void manageDelete(String[] headerSplit) {
        // DELETE <Version> <SenderId> <FileId> <CRLF><CRLF>
        System.out.println("Received delete of file : " + headerSplit[3]);
        Peer.getStorage().deleteAllChunksOfFile(headerSplit[3]);
        String pathBackup = "../PeerStorage/peer" + Peer.getId() + "/" + "backup";
        File backupDir = new File(pathBackup);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        String pathFileId = pathBackup + "/" + headerSplit[3];
        File pathToFile = new File(pathFileId);
        File[] pathFiles = pathToFile.listFiles();
        if (pathFiles != null) {
            for (File file : pathFiles) {
                System.out.println("Deleting chunkNr : " + file.getPath());
                file.delete();
            }
        }
        pathToFile.delete();
    }

    private synchronized void manageRemoved() {

    }

    public String getHeaderPacket() {
        String response = "";

        ByteArrayInputStream stream = new ByteArrayInputStream(this.packet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
