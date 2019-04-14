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

public class Reclaim implements Runnable{
    private int reclaimSize;

    public Reclaim(int size) {
      this.reclaimSize = size;
    }
    
    @Override
    public void run() {
        int memoryUsed = (int) Peer.getStorage().getMemoryUsed();

        if(memoryUsed <= reclaimSize) {
            Peer.getStorage().setMemoryFree(reclaimSize - memoryUsed);
            System.out.println("RECLAIM done");
			return;
        }

        ArrayList<Chunk> chunks = Peer.getStorage().getChunksStored();
        long memoryDiference = (memoryUsed  - reclaimSize) * 1000;

        try{
            for(Chunk chunk : chunks){

                String headerAux = "REMOVED " + Peer.getVersion() + " " + Peer.getId() + " " + chunk.getchunkID() + " " + 
                Utility.CRLF + Utility.CRLF;
                byte[] message = headerAux.getBytes("US-ASCII");

                Peer.getMC().message(message);
                String pathBackup = "../PeerStorage/peer" + Peer.getId() + "/" + "backup";
                File backupDir = new File(pathBackup);
                if (!backupDir.exists()) {
                    continue;
                }
                String pathFileId = pathBackup + "/" + chunk.getFileID();
                File pathToFile = new File(pathFileId);
                if (!pathToFile.exists()) {
                    continue;
                }
                String chunkDir = pathToFile + "/" + "chunk" + chunk.getchunkID();
                File chunkFile = new File(chunkDir);
                if (!chunkFile.exists()) {
                    continue;
                }
                chunkFile.delete();
                
                Peer.getStorage().deleteChunk(chunk);

                memoryDiference -= chunk.getData().length;
                memoryUsed -= chunk.getData().length;

                if(memoryDiference <= 0){
                    break;
                }
            }
        }catch (IOException e) {
                e.printStackTrace();
        }

        Peer.getStorage().setMemoryFree(reclaimSize);
        Peer.getStorage().setMemoryUsed(memoryUsed);
    }
}