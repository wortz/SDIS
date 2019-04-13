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
            manageStored(headerSplit, header.length());
            break;
        case "GETCHUNK":
            manageGetChunk();
            break;
        case "CHUNK":
            manageChunk();
            break;
        case "DELETE":
            manageDelete();
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
        Peer.getStorage().addProcessingChunk(chunk);
        try {
            String storedResponse = "STORED " + headerSplit[1] + " " + Peer.getId() + " " + fileId + " " + chunkNr
                    + Utility.CRLF + Utility.CRLF;
            byte[] stored = storedResponse.getBytes("US-ASCII");
            Thread.sleep(Utility.getRandomValue(Utility.MAX_WAIT_TIME));
            if (!Peer.getStorage().finishedDegree(chunkNr, fileId)) {
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
            }else{
                Peer.getStorage().removeProcessingChunk(chunk);
            }

        } catch (IOException e) {
            System.out.println("Error sending Stored message.");
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void manageStored(String[] headerSplit, int headerLength) {
        // STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        System.out.println("Received stored " + headerSplit[4]);
        Peer.getStorage().incRepDegree(Integer.parseInt(headerSplit[4]), headerSplit[3], headerSplit[2]);
    }

    private synchronized void manageGetChunk() {

    }

    private synchronized void manageChunk() {

    }

    private synchronized void manageDelete() {

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
