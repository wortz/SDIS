package channels;

import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

public class Channel implements Runnable {

    protected InetAddress address;
    protected int port;
    protected MulticastSocket socket;

    public Channel(String address, int port) throws IOException {
        this.address = InetAddress.getByName(address);
        this.port = port;

        this.socket = new MulticastSocket(port);
        this.socket.joinGroup(this.address);

        System.out.println("Joined Multicast Channel " + address + ":" + port);
    }

    @Override
    public void run() {
        byte[] buf = new byte[65507];
        while(true) {
            this.socket.joinGroup(this.address);
            DatagramPacket multicastPacket = new DatagramPacket(buf, buf.length);

            try {
                this.socket.receive(multicastPacket);
                byte[] msg= Arrays.copyOfRange(multicastPacket.getData(), 0, multicastPacket.getLength());
                MessageHandler handler=new MessageHandler(msg);
                Peer.getExec().execute(handler);
                this.socket.close();

                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void message(byte[] message) throws IOException{
        this.socket.send(new DatagramPacket(message, message.length, address, port));
    }

}