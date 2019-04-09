package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public abstract class Channel implements Runnable {

    protected InetAddress address;
    protected int port;

    protected MulticastSocket socket;

    public Channel(String address, int port) throws IOException {
        this.socket = new MulticastSocket(port);
        this.socket.setTimeToLive(1);
        this.address = InetAddress.getByName(address);
        this.port = port;
        socket.joinGroup(this.address);
        System.out.println("Joined Multicast Channel " + address + ":" + port);
    }

    @Override
    public void run() {
        byte[] buf = new byte[65000];

        while(true) {
            DatagramPacket multicastPacket = new DatagramPacket(buf, buf.length);

            try {
                this.socket.receive(multicastPacket);
                parseMessage(multicastPacket);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void parseMessage(DatagramPacket packet) {
        String request = new String(packet.getData()).trim();
        System.out.println(request);
    }

    public void message(String message) throws IOException{
        byte[] buf = message.getBytes();
        this.socket.send(new DatagramPacket(buf, buf.length, address, port));
    }

}