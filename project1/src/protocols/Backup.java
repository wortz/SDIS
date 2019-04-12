package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;


import utility.Utility;
import file.*;
import peer.*;
import channels.*;


public class Backup implements Runnable{
	
	protected String path;
	protected int replicationDegree;
	protected Peer peer;

	
	public Backup(Peer peer, String file_path, int replicationDegree) {
		this.path = file_path;
		this.peer = peer;
		this.replicationDegree = replicationDegree;
	}

	@Override
	public void run() {
		File file = new File(this.path);
        String fileID = Utility.getFileSHA(file);

        try {
            String headerAux = "PUTCHUNK " + peer.getVersion() + " " + peer.getId() + " " + fileID + " ";
            ArrayList<byte[]> chunks = Utility.getChunks(this.path);

            for (int i = 0; i < chunks.size(); i++) {
                String restMessage = headerAux + (i + 1) + " " + this.replicationDegree + " " + Message.CRLF + Message.CRLF;
                byte[] body = chunks.get(i);
                byte[] header = restMessage.getBytes("US-ASCII");
                byte[] message = new byte[header.length+body.length];
                System.arraycopy(header, 0, message, 0, header.length);
                System.arraycopy(body, 0, message, header.length, body.length);
                System.out.println("PUTCHUNK SENT OF FILE : " + fileID + " with chunk number : " + (i+1));
                while(peer.getStorage())
                sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	
    }
    
    public synchronized void sendMessage(byte[] message){
        while(peer)
    }
	
}