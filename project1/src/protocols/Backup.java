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

	
	public Backup(String file_path, int replicationDegree) {
		this.path = file_path;
		this.replicationDegree = replicationDegree;
	}

	@Override
	public void run() {
		File file = new File(this.path);
        String fileID = Utility.getFileSHA(file);
        //creates filedata
        FileData filedata = new FileData(this.path, fileID, file.getName());

        try {
            String headerAux = "PUTCHUNK " + Peer.getVersion() + " " + Peer.getId() + " " + fileID + " ";
             ArrayList<byte[]> chunks = Utility.getChunks(this.path);

            for (int i = 0; i < chunks.size(); i++) {
                String restMessage = headerAux + (i + 1) + " " + this.replicationDegree + " " + Utility.CRLF + Utility.CRLF;
                byte[] body = chunks.get(i);

                byte[] header = restMessage.getBytes("US-ASCII");
                byte[] message = new byte[header.length+body.length];
                System.arraycopy(header, 0, message, 0, header.length);
                System.arraycopy(body, 0, message, header.length, body.length);

                //Creates a chunk and adds it to the processing array in storage
                Chunk chunk = new Chunk(i+1, body, this.replicationDegree, fileID, Peer.getId());
                Peer.getStorage().addProcessingChunk(chunk);

                int numberOfTries=0;
                int waitTime=Utility.INITIAL_WAIT_TIME;
                //while that chunk in processing array is not with desired rep degree it continues to try
                while(!Peer.getStorage().finishedDegree(i+1, fileID)){
                    if(numberOfTries == Utility.PUTCHUNK_TRIES){
                        System.out.println("Failed to backup file: " + fileID);
                        Peer.getStorage().removeProcessingChunk(chunk);
                        return;
                    }
                    System.out.println("PutChunk : " + (i + 1));
                    Peer.getMDB().message(message);
                    numberOfTries++;
                    Thread.sleep(waitTime);
                    waitTime*=2;
                }
                //adds the current degree of chunk to filedata
                filedata.addChunk(Peer.getStorage().getChunkCurDegree(chunk));
                Peer.getStorage().removeProcessingChunk(chunk);
            }
            Peer.getStorage().addFileData(filedata);
        } catch (IOException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
		
	
    }
}