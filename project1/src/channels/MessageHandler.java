import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.util.io.FileInputStream;
import java.util.io.ByteArrayInputStream;
import java.util.BufferedReader;

public class MessageHandler implements Runnable {

    protected byte[] msgBytes;

    public MessageHandler(byte[] msgBytes) {
        this.msgBytes = msgBytes;
    }

    public void run() {
        String message = new String(this.msgBytes, 0, this.msgBytes.length);
        String trimMessage = message.trim();
        String[] msgArray = trimMessage.split(" ");

        switch (msgArray[0]) {
            case "PUTCHUNK":
                managePutchunk();
                break;
            case "STORED":
                manageStored();
                break;
        /*
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
        */
        }
    }


    private synchronized void managePutchunk(){

    }

    private synchronized void manageStored(){

    }

    public static String[] manageHeader(DatagramPacket packet){
        String response = "";

        ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try{
            response = reader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }

        return response;
    }



}
