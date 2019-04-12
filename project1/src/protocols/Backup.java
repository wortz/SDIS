package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;


import utility.Utility;
import file.Chunk;
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
		File file = new File(path);
        String fileID = Utility.getFileSHA(file);

        try {
            String headerAux = "PUTCHUNK " + peer.getVersion() + " " + peer.getId() + " " + fileID + " ";
            ArrayList<byte[]> chunks = Utility.getChunks(this.path);
            System.out.println(chunks.size());

            for (int i = 0; i < chunks.size(); i++) {
                String restMessage = headerAux + (i + 1) + " " + replicationDegree + " " + Message.CRLF + Message.CRLF;
                byte[] body = chunks.get(i);
                byte[] header = restMessage.getBytes("US-ASCII");
                byte[] message = new byte[header.length+body.length];
                System.arraycopy(header, 0, message, 0, header.length);
                System.arraycopy(body, 0, message, header.length, body.length);
                Message msg=new Message(message, peer.getMDB());
                peer.getExec().execute(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	
	}
	
}