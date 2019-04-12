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
    protected Channel MDB;

	
	public Backup(Peer peer, String file_path, int replicationDegree, Channel MDB) {
		this.path = file_path;
		this.peer = peer;
		this.replicationDegree = replicationDegree;
        this.MDB=MDB;
	}

	@Override
	public void run() {
		File file = new File(this.path);
        String fileID = Utility.getFileSHA(file);

        try {
            String headerAux = "PUTCHUNK " + peer.getVersion() + " " + this.peer.getId() + " " + fileID + " ";
            ArrayList<byte[]> chunks = Utility.getChunks(this.path);

            for (int i = 0; i < chunks.size(); i++) {
                String restMessage = headerAux + (i + 1) + " " + this.replicationDegree + " " + Utility.CRLF + Utility.CRLF;
                byte[] body = chunks.get(i);
                byte[] header = restMessage.getBytes("US-ASCII");
                byte[] message = new byte[header.length+body.length];
                System.arraycopy(header, 0, message, 0, header.length);
                System.arraycopy(body, 0, message, header.length, body.length);
                Chunk chunk = new Chunk(i+1, body, this.replicationDegree, fileID, this.peer.getId());
                peer.getStorage().addProcessingChunk(chunk);
                int numberOfTries=0;
                int waitTime=Utility.INITIAL_WAIT_TIME;
                while(!peer.getStorage().finishedDegree(i+1, fileID)){
                    if(numberOfTries==Utility.PUTCHUNK_TRIES){
                        System.out.println("Failed to backup file: " + fileID);
                        return;
                    }
                    sendMessage(message);
                    System.out.println("PUTCHUNK SENT OF FILE : " + fileID + " with chunk number : " + (i+1));
                    numberOfTries++;
                    Thread.sleep(waitTime);
                    waitTime*=2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
		
	
    }
    
    public synchronized void sendMessage(byte[] message) throws IOException{
            this.MDB.message(message);
    }
	
}