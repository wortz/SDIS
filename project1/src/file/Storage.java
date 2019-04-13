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
	private ArrayList<FileData> filesStored;

	private long memoryUsed;
	private long memoryFree;
	private ArrayList<Chunk> processingChunks;
	private ArrayList<Chunk> restoreChunks;

	private ArrayList<Chunk> chunks;

	public Storage() {
		memoryFree = 10000000;
		memoryUsed = 0;
		chunks = new ArrayList<Chunk>();
		processingChunks = new ArrayList<Chunk>();
		filesStored = new ArrayList<FileData>();
		restoreChunks = new ArrayList<Chunk>();
	}

	public long getMemoryUsed() {
		return memoryUsed;
	}

	public long getMemoryFree() {
		return memoryFree;
	}

	public synchronized ArrayList<Chunk> getChunksStored() {
		return chunks;
	}

	public ArrayList<FileData> getStoredFiles(){
		return this.filesStored;
	}

	public ArrayList<Chunk> getRestoredChunks(){
		return restoreChunks;
	}

	public synchronized boolean alreadyRestored(Chunk chunk){
		for(int i = 0; i < this.restoreChunks.size(); i++){
			if(this.restoreChunks.get(i).compareChunk(chunk.getchunkID(), chunk.getFileID())){
				this.restoreChunks.remove(i);
				return true;
			}
		}
		return false;
	}

	public synchronized boolean restored(int chunkID, String fileID){
		for(Chunk resChunk : restoreChunks){
			if(resChunk.compareChunk(chunkID, fileID))
				return true;
		}
		return false;
	}

	public synchronized void addRestored(Chunk chunk){
		for(int i = 0; i < this.restoreChunks.size(); i++){
			if(this.restoreChunks.get(i).compareChunk(chunk.getchunkID(), chunk.getFileID()))
				return;
		}
		restoreChunks.add(chunk);
		return;
	}

	public synchronized Chunk getRestoredChunk(int chunkNr,String fileID){
		for(Chunk resChunk : restoreChunks){
			if(resChunk.compareChunk(chunkNr, fileID))
				return resChunk;
		}
		return null;
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

	public synchronized void deleteAllChunksOfFile(String fileID){
		for(int i=0; i<this.chunks.size();i++){
			if(this.chunks.get(i).getFileID().equals(fileID)){
				chunks.remove(i);
				i--;
			}
		}
	}
	
	public synchronized void addFileData(FileData filedata){
		for(int i = 0; i < this.filesStored.size(); i++){
			if(this.filesStored.get(i).getPath().equals(filedata.getPath())){
				this.filesStored.remove(i);
				break;
			}
		}
		this.filesStored.add(filedata);
	}

	public synchronized void removeFileData(int i){
		this.filesStored.remove(i);
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

	public synchronized void updateProcessingChunk(Chunk chunk){
		for (int i = 0; i < processingChunks.size(); i++) {
			if (processingChunks.get(i).compareChunk(chunk.getchunkID(), chunk.getFileID())) {
				processingChunks.remove(i);
				storeChunk(chunk);
				return;
			}
		}
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

	public synchronized int getChunkCurDegree(Chunk chunk){
		for (int i = 0; i < this.processingChunks.size(); i++) {
			if (processingChunks.get(i).compareChunk(chunk.getchunkID(), chunk.getFileID())) {
				return chunk.getActDegree();
			}
		}
		return 0;
	}

}