package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import utility.Utility;;

public class Storage implements Serializable{
		
    private static final long serialVersionUID = 1L;
    
    private long memoryUsed;
	private long memoryFree;
	
	private ArrayList<Chunk> chunks;
	
	public Storage() {
		memoryFree = 10000000;
		memoryUsed = 0;
		chunks = new ArrayList<Chunk>();
	}

	public long getMemoryUsed() {
		return memoryUsed; 
    }
    
    public long getMemoryFree(){
        return memoryFree;
    }

    public ArrayList<Chunk> getChunksStored() {
		return chunks;
	}
    
    
	public synchronized boolean storeChunk(Chunk chunk) {
		byte[] chunk_data = chunk.getData();
		
		long tmp = memoryFree - chunk_data.length;
		
		if(tmp <0) {
			System.out.println("Chunk can't be stored.");
			return false;
		}
        
        memoryFree -= chunk_data.length;
		memoryUsed += chunk_data.length;
        
/*        
		File folder = new File(-----);

		if (!(folder.exists() && folder.isDirectory()))
			folder.mkdir();

		FileOutputStream out;
		
        chunks.add(chunk);
        */

        return false;
    }
    
	public synchronized void incRepDegree(String chunkID,String fileID) {
		
		
		for(int i=0; i< chunks.size();i++) {
			if(chunks.get(i).compareChunk(chunkID, fileID)) {
				chunks.get(i).incAdcDegree();
				break;
			}
		}
		
		/*try {
			//Peer.saveDisk();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	
	}
	
}