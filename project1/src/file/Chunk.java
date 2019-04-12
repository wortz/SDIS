package file;

public class Chunk{
    private String chunkID;
    private byte[] data;
    private int replicationDegree;
    private String fileID;
    private String senderID;
    private int actReplicationDegree;

    public Chunk(String chunkID, byte[] data, int replicationDegree, String fileID, String senderID) {
        this.chunkID = chunkID;
        this.data = data;
        this.replicationDegree = replicationDegree;
        this.fileID = fileID;
        this.senderID = senderID;
        this.actReplicationDegree = 0;
    }

    public String getchunkID() {
        return this.chunkID;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }

    public boolean compareChunk(String chunkID, String fileID){
        if(this.chunkID.equals(chunkID) && this.fileID.equals(fileID))
            return true;
        return false;
    }

    public void incAdcDegree(){
        this.actReplicationDegree++;
    }

    public String getFileID() {
        return this.fileID;
    }

    public String getSenderID() {
        return this.senderID;
    }


}