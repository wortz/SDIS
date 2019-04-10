package channels;

import java.net.DatagramPacket;

public class Message  implements Runnable {
    public static String CRLF = "\\r\\n";
    public Channel multicastChannel;
    public String message;
            
    public Message (String message, Channel multicastChannel){
        this.message = message;
        this.multicastChannel = multicastChannel;
    }

    @Override
    public void run(){
        this.multicastChannel.message(message);
    }
            
}