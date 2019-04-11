package file;

public class Chunk{
    private int index;
    private byte[] data;
    private int replicationDegree;
    private String fileID;
    private String senderID;
    private int act_replicationDegree;

    public Chunk(int index, byte[] data, int replicationDegree, String fileID, int senderID) {
        this.index = index;
        this.data = data;
        this.replicationDegree = replicationDegree;
        this.fileID = fileID;
        this.senderID = senderID;
    }

    public int getIndex() {
        return index;
    }

    public byte[] getData() {
        return data;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }

    public String getFileID() {
        return fileID;
    }

    public int getSenderID() {
        return senderID;
    }


}