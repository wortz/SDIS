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
        this.address = InetAddress.getByName(address);
        this.port = port;

        socket = new MulticastSocket(port);
        socket.setTimeToLive(1);
        socket.joinGroup(this.address);

        System.out.println("Joined Multicast Channel " + address + ":" + port);
    }

    @Override
    public void run() {
        byte[] buf = new byte[65507];

        while(true) {
            DatagramPacket multicastPacket = new DatagramPacket(buf, buf.length);

            try {
                this.socket.receive(multicastPacket);
                parseMessage(multicastPacket);


                // 1 ioption
                //this.parentPeer.MessageHandler(packet.getData(), packet.getLength());


                //2 option
                //Peer.getExec().execute(new MessageHandler(multicastPacket));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //this.socket.close();
    }

    public void parseMessage(DatagramPacket packet) {
        String request = new String(packet.getData()).trim();
            //System.out.println(request);
    }

    public void message(byte[] message) throws IOException{
        System.out.println(message.length);
        this.socket.send(new DatagramPacket(message, message.length, address, port));
    }

}