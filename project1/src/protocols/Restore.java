package protocols;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;

import utility.Utility;
import file.*;
import peer.*;
import channels.*;

public class Restore implements Runnable {
    protected String path;

    /**
     * constructor for Restore protocol
     * @param path path of the file to be restored
     */
    public Restore(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        for (int i = 0; i < Peer.getStorage().getStoredFiles().size(); i++) {
            if (Peer.getStorage().getStoredFiles().get(i).getPath().equals(this.path)) {
                try {
                    String fileID = Peer.getStorage().getStoredFiles().get(i).getfileID();
                    String fileName = Peer.getStorage().getStoredFiles().get(i).getFileName();
                    int numberOfchunks = Peer.getStorage().getStoredFiles().get(i).getNrChunks();
                    //sends a number of getchunks equals to the number of chunks for that file
                    for (int n = 0; n < numberOfchunks; n++) {
                        String header = "GETCHUNK " + Peer.getVersion() + " " + Peer.getId() + " " + fileID + " "
                                + (n + 1) + " " + Utility.CRLF + Utility.CRLF;
                        System.out.println("Sent " + header);
                        try {
                            Peer.getMC().message(header.getBytes("US-ASCII"));
                            Thread.sleep(Utility.INITIAL_WAIT_TIME);
                            if (!Peer.getStorage().restored((i + 1), fileID)) {
                                System.out.println("Failed to receive chunkNr: " + (i + 1));
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    int numberRestoredChunks = Peer.getStorage().getRestoredChunks().size();
                    String pathRestore = "../PeerStorage/peer" + Peer.getId() + "/" + "restore";
                    File restoreDir = new File(pathRestore);
                    if (!restoreDir.exists()) {
                        restoreDir.mkdirs();
                    }
                    String restoreFileDir = pathRestore + "/" + fileName;
                    File restoreFile = new File(restoreFileDir);
                    if (restoreFile.exists()) {
                        restoreFile.delete();
                    }
                    //joins all data
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    for (int chunknr = 1; chunknr <= numberRestoredChunks; chunknr++) {
                        Chunk chunk = Peer.getStorage().getRestoredChunk(chunknr, fileID);
                        os.write(chunk.getData());
                    }
                    byte[] data=os.toByteArray();
                    //creates and writes the restored file
                    FileOutputStream fos = new FileOutputStream(restoreFile);
                    fos.write(data);
                } catch (IOException e) {
                    System.out.println("Error sending Stored message.");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        System.out.println("File not found on this peer.");
    }
}