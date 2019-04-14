package file;

import java.util.ArrayList;

public class FileData {
    private String path;
    private String fileName;
    private String fileID;
    //used to store what is the actual rep degree of each chunk
    private ArrayList<Integer> chunksDegree;

    /**
     * Constructor for fileData
     * @param path original path of the file
     * @param fileID sha256 fileID
     * @param fileName fileName
     */
    public FileData(String path, String fileID, String fileName){
        this.path = path;
        this.fileID = fileID;
        this.fileName=fileName;
        this.chunksDegree = new ArrayList<Integer>();
    }

    public synchronized String getFileName(){
        return this.fileName;
    }

    public synchronized String getPath(){
        return this.path;
    }

    public synchronized String getfileID(){
        return this.fileID;
    }

    public synchronized int getNrChunks(){
        return chunksDegree.size();
    }

    public synchronized void addChunk(int degree){
        this.chunksDegree.add(degree);
    }
}