package channels;

import java.net.DatagramPacket;

public class Message {
    public static String CRLF = "\\r\\n";
    public String messageType;
    public String version;
    public int senderID;
    public String fileID;
    public int chunkNr=0;
    public int replicationDegree=0;
    public byte[] body = null;
            
    public Message (String message){
        String[] comp = message.split("\\R\\R", 2);
        String[] header = comp[0].split("\\s+");
        messageType=header[0];
        if(messageType=="PUTCHUNK"){
            this.replicationDegree = Integer.parseInt(header[5]);
        }
        this.version = header[1];
        this.senderID = Integer.parseInt(header[2]);
        this.fileID = header[3];
        if(header.length>5)
            chunkNr = Integer.parseInt(header[4]);

        if(comp.length > 1)
            this.body = comp[1].getBytes();

    }
    public String getMessageString() {
        StringBuffer buff = new StringBuffer();
        buff.append(this.messageType + " " + this.version + " " + this.senderID + " " + this.fileID + " ");
        if(this.chunkNr != 0)
            buff.append(this.chunkNr + " ");
        if(this.replicationDegree != 0)
            buff.append(this.replicationDegree + " ");
        buff.append(Message.CRLF+Message.CRLF);
        if(this.body != null)
            buff.append(this.body);
    return buff.toString();
    }
            
}