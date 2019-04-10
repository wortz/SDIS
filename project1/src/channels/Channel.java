package channels;

import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Channel implements Runnable {

    protected InetAddress address;
    protected int port;
    protected MulticastSocket socket;
    protected Peer peer;

    public Channel(String address, int port, Peer peer) throws IOException {
        this.address = InetAddress.getByName(address);
        this.port = port;
        this.peer = peer;

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

                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //this.socket.close();
    }

    public void parseMessage(DatagramPacket packet) {
        String request = new String(packet.getData()).trim();
        if(request.contains("PUTCHUNK")){
            peer.handlePutChunk();
        }else{
            System.out.println(request);
        }
    }

    public synchronized void message(byte[] message) throws IOException{
        this.socket.send(new DatagramPacket(message, message.length, address, port));
    }

}