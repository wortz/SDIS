package protocols;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;

import utility.Utility;
import file.*;
import peer.*;
import channels.*;

public class Restore implements Runnable {
    protected String path;

    public Restore(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        for (int i = 0; i < Peer.getStorage().getStoredFiles().size(); i++) {
            if (Peer.getStorage().getStoredFiles().get(i).getPath().equals(this.path)) {
                try {
                    String fileID = Peer.getStorage().getStoredFiles().get(i).getfileID();
                    int numberOfchunks = Peer.getStorage().getStoredFiles().get(i).getNrChunks();
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
                    String pathBackup = "../PeerStorage/peer" + Peer.getId() + "/" + "restore";
                    File backupDir = new File(pathBackup);
                    if (!backupDir.exists()) {
                        backupDir.mkdirs();
                    }
                    String pathFileId = pathBackup + "/" + fileID;
                    File pathToFile = new File(pathFileId);
                    if (!pathToFile.exists()) {
                        pathToFile.mkdir();
                    }
                    for (int chunknr = 1; chunknr <= numberOfchunks; chunknr++) {
                        Chunk chunk = Peer.getStorage().getRestoredChunk(chunknr, fileID);
                        String restoreFileDir = pathToFile + "/" + "chunk" + chunknr;
                        File restoreFile = new File(restoreFileDir);
                        if (restoreFile.exists()) {
                            restoreFile.delete();
                        }
                        if (!restoreFile.exists()) {
                            FileOutputStream fos = new FileOutputStream(restoreFile);
                            fos.write(chunk.getData());
                        }
                        return;

                    }
                } catch (IOException e) {
                    System.out.println("Error sending Stored message.");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("File not found on this peer.");
    }
}