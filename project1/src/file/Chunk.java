package file;

import java.util.ArrayList;

public class Chunk {
    private int chunkID;
    private byte[] data;
    private int replicationDegree;
    private String fileID;
    private String senderID;
    private ArrayList<String> IDStored;

    public Chunk(int chunkID, byte[] data, int replicationDegree, String fileID, String senderID) {
        this.chunkID = chunkID;
        this.data = data;
        this.replicationDegree = replicationDegree;
        this.fileID = fileID;
        this.senderID = senderID;
        this.IDStored = new ArrayList<String>();
    }

    public int getchunkID() {
        return this.chunkID;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getReplicationDegree() {
        return this.replicationDegree;
    }

    public boolean reachedDegree(){
        return (this.IDStored.size()==this.replicationDegree);
    }

    public boolean compareChunk(int chunkID, String fileID) {
        if (this.chunkID == chunkID && this.fileID.equals(fileID))
            return true;
        return false;
    }

    public void addStored(String id) {
        for(int i=0; i<this.IDStored.size();i++){
            if(this.IDStored.get(i).equals(id))
                return;
        }
        this.IDStored.add(id);
    }

    public String getFileID() {
        return this.fileID;
    }

    public String getSenderID() {
        return this.senderID;
    }

}