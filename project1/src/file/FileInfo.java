package file;

import utility.Utility;

import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.util.ArrayList;



public class FileInfo implements Serializable {

    protected String fileID;
    protected File file;
    protected int replicationDegree;
    protected ArrayList<Chunk> chunks;

    public FileData(String path, int replicationDegree) {
        this.fileID = new File(path);
        this.replicationDegree = replicationDegree;
        this.chunks = new ArrayList<>();
        splitFile();
        hashedID();
    }
/*
    private void splitFile() {
        int chunkNr = 0;

        byte[] buffer = new byte[BUF_SIZE];

        try (FileInputStream fis = new FileInputStream(this.file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                byte[] body = Arrays.copyOf(buffer, bytesAmount);

                chunkNr++;
                Chunk chunk = new Chunk(chunkNr, body, bytesAmount);
                this.chunks.add(chunk);
                buffer = new byte[sizeOfChunks];
            }

            if (this.file.length() % BUF_SIZE == 0) {
                Chunk chunk = new Chunk(chunkNr, null, 0);
                this.chunks.add(chunk);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    private void hashedID() {
        String fileName = this.file.getName();
        //String dateModified = String.valueOf(this.file.lastModified());
        String owner = this.file.getParent();

        // + '-' + dateModified
        String id = fileName + '-' + owner;

        this.fileID = sha256(fileID);
    }




}