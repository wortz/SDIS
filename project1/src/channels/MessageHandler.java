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
    protected Peer peer;

    public MessageHandler(byte[] packet, Peer peer) {
        System.out.println("Um handler");
        this.packet = packet;
        this.peer = peer;
    }

    public void run() {
        String header = getHeaderPacket();
        String[] headerSplit = header.split(" ");
        if (headerSplit[2].equals(this.peer.getId()))
            return;
        switch (headerSplit[0]) {
        case "PUTCHUNK":
            managePutchunk(headerSplit,header.length());
            break;
        case "STORED":
            manageStored(headerSplit,header.length());
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
        //PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
        byte[] body = new byte[(this.packet.length - headerLength - 4)];
        System.arraycopy(this.packet, headerLength + 4, body, 0, body.length);
        Chunk chunk = new Chunk(Integer.parseInt(headerSplit[4]), body, Integer.parseInt(headerSplit[5]), headerSplit[3], headerSplit[2]);
        chunk.addStored(this.peer.getId());
        this.peer.getStorage().storeChunk(chunk);
        System.out.println("Stored chunk : " + headerSplit[4] + " from file : " + headerSplit[3]);
        try{
        String storedResponse = "STORED " + headerSplit[1] + " " + this.peer.getId() + " " + headerSplit[3] + " " + headerSplit[4] + Utility.CRLF + Utility.CRLF;
        byte[] stored = storedResponse.getBytes("US-ASCII");
        Message storedMessage = new Message(stored, this.peer.getMC());
        this.peer.getExec().schedule(storedMessage, Utility.getRandomValue(Utility.MAX_WAIT_TIME), TimeUnit.MILLISECONDS);
        System.out.println("Stored message sent of file: " + headerSplit[3] + " with chunk number : " + headerSplit[4]);
        }catch(IOException e){
            System.out.println("Error sending Stored message.");
            e.printStackTrace();
        }
    }

    private synchronized void manageStored(String[] headerSplit, int headerLength) {
        //STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        this.peer.getStorage().incRepDegree(Integer.parseInt(headerSplit[4]), headerSplit[3], headerSplit[2]);
        System.out.println("Stored message received : " + headerSplit[2] + " " + headerSplit[3] + " " + headerSplit[4]);
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
