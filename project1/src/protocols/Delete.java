package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;

import peer.*;
import channels.*;
import utility.Utility;
import file.*;



public class Delete implements Runnable{
    private String file_path;

    public Delete(String file_path) {
      this.file_path = file_path;
    }
    
    public void run() {
      for(int i = 0; i < Peer.getStorage().getStoredFiles().size(); i++){
      System.out.println(Peer.getStorage().getStoredFiles().get(i).getPath());
        if(Peer.getStorage().getStoredFiles().get(i).getPath().equals(this.file_path)){
          for(int t=0;t<Utility.DELETE_TRIES;t++){
            String header = "DELETE " + Peer.getVersion() + " " + Peer.getId() + " " + Peer.getStorage().getStoredFiles().get(i).getfileID() + " " + Utility.CRLF + Utility.CRLF;
            System.out.println("Sent DELETE of file " + Peer.getStorage().getStoredFiles().get(i).getfileID());
            try{
            Peer.getMC().message(header.getBytes("US-ASCII"));
            }catch(IOException e){
              e.printStackTrace();
            }
            Peer.getStorage().removeFileData(i);
          }
          return;
        }
      }
      System.out.println("File not found on this peer.");
    }
}