package channels;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

public class BackupChannel implements Runnable {
    private InetAddress address;
    private int portMDB;

    private MulticastSocket socket;

    public BackupChannel(String MDB, int portMDB){

        this.socket = new MulticastSocket(port);
        this.socket.setTimeToLive(1);

        address = InetAddress.getByName(MDB);
        this.portMC = portMC;

        socket.joinGroup(this.address);

        System.out.println("Multicast Channel" + address + " "  + portMDB);

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