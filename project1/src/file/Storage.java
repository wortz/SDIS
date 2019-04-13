package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import utility.Utility;;

public class Storage implements Serializable {

	private static final long serialVersionUID = 1L;

	private long memoryUsed;
	private long memoryFree;
	private ArrayList<Chunk> processingChunks;

	private ArrayList<Chunk> chunks;

	public Storage() {
		memoryFree = 10000000;
		memoryUsed = 0;
		chunks = new ArrayList<Chunk>();
		processingChunks = new ArrayList<Chunk>();
	}

	public long getMemoryUsed() {
		return memoryUsed;
	}

	public long getMemoryFree() {
		return memoryFree;
	}

	public ArrayList<Chunk> getChunksStored() {
		return chunks;
	}

	public synchronized boolean storeChunk(Chunk chunk) {
		byte[] chunk_data = chunk.getData();

		long tmp = memoryFree - chunk_data.length;

		if (tmp < 0) {
			System.out.println("Chunk can't be stored(memory).");
			return false;
		}

		for (int i = 0; i < this.chunks.size(); i++) {
			if (chunks.get(i).compareChunk(chunk.getchunkID(), chunk.getFileID())) {
				System.out.println("Already have that chunk");
				return true;
			}
		}

		memoryFree -= chunk_data.length;
		memoryUsed += chunk_data.length;

		/*
		 * File folder = new File(-----);
		 * 
		 * if (!(folder.exists() && folder.isDirectory())) folder.mkdir();
		 * 
		 * FileOutputStream out;
		 * 
		 * chunks.add(chunk);
		 */

		return false;
	}

	public synchronized void incRepDegree(int chunkID, String fileID, String id) {

		for (int i = 0; i < this.processingChunks.size(); i++) {
			if (processingChunks.get(i).compareChunk(chunkID, fileID)) {
				processingChunks.get(i).addStored(id);
				return;
			}
		}

		for (int i = 0; i < this.chunks.size(); i++) {
			if (chunks.get(i).compareChunk(chunkID, fileID)) {
				chunks.get(i).addStored(id);
				break;
			}
		}
		/*
		 * try { //Peer.saveDisk(); } catch (IOException e) { e.printStackTrace(); }
		 */

	}

	public synchronized void removeProcessingChunk(Chunk chunk) {
		for (int i = 0; i < processingChunks.size(); i++) {
			if (processingChunks.get(i).compareChunk(chunk.getchunkID(), chunk.getFileID())) {
				processingChunks.remove(i);
				return;
			}
		}
		System.out.println("Chunk not found to be removed.");
	}

	public synchronized boolean finishedDegree(int chunkID, String fileID) {
		for (int i = 0; i < this.processingChunks.size(); i++) {
			if (processingChunks.get(i).compareChunk(chunkID, fileID)) {
				return processingChunks.get(i).reachedDegree();
			}
		}
		System.out.println("Error, chunk not found on finishedDegree");
		return false;
	}

	public synchronized void addProcessingChunk(Chunk chunk) {
		this.processingChunks.add(chunk);
	}

}