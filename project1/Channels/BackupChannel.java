package Channels;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class BackupChannel implements Runnable {
    private InetAddress address;
    private int portMC;

    public BackupChannel(String MC, int portMC){
        try {
            // Get the address that we are going to connect to.
            address = InetAddress.getByName(MC);
            this.portMC = portMC;
        } catch (UnknownHostException e) {
        e.printStackTrace();
        }

    }

    public void message(byte[] msg) {
        try (DatagramSocket senderSocket = new DatagramSocket()){
            DatagramPacket msgPacket = new DatagramPacket(msg, msg.length, address, portMC);
            senderSocket.send(msgPacket);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run(){
        byte[] buf = new byte[65000];

        try (MulticastSocket receiverSocket = new MulticastSocket(portMC)){
            receiverSocket.joinGroup(address);

            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                receiverSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println(msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}