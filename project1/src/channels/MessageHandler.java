package channels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.io.InputStreamReader;
import peer.Peer;



public class MessageHandler implements Runnable {

    protected DatagramPacket packet;
    protected String[] messageParts;

    public MessageHandler(DatagramPacket packet) {
        this.packet = packet;
    }

    public void run() {
        messageParts = managePacket(packet);

        int sender_id = Integer.parseInt(messageParts[2]);

        // if message comes from self ignore it
        if(sender_id == Peer.getId()) return;

        switch (messageParts[0]) {
            case "PUTCHUNK":
                managePutchunk();
                break;
            case "STORED":
                manageStored();
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


    private synchronized void managePutchunk(){

    }

    private synchronized void manageStored(){

    }

    private synchronized void manageGetChunk(){

    }

    private synchronized void manageChunk(){

    }

    private synchronized void manageDelete(){

    }

    private synchronized void manageRemoved(){

    }

    public static String[] managePacket(DatagramPacket packet){
        String response = "";

        ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try{
            response = reader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }

        return response.split(" ");
    }



}
