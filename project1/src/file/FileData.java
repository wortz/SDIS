package file;

import java.util.ArrayList;

public class FileData {
    private String path;
    private String fileID;
    private ArrayList<Integer> chunksDegree;

    public FileData(String path, String fileID){
        this.path = path;
        this.fileID = fileID;
        this.chunksDegree = new ArrayList<Integer>();
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