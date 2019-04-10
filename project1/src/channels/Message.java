package channels;

import java.net.DatagramPacket;

public class Message  implements Runnable {
    public static String CRLF = "\\r\\n";
    public Channel multicastChannel;
    public byte[] message;
            
    public Message (byte[] message, Channel multicastChannel){
        this.message = message;
        this.multicastChannel = multicastChannel;
    }

    @Override
    public void run(){
        try {
            this.multicastChannel.message(message);
        }catch (Exception e) {
            System.err.println("Message exception: " + e.toString());
            e.printStackTrace();
        }
    }
            
}