package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;


import utility.Utility;
import file.*;
import peer.*;
import channels.*;

public class Restore implements Runnable{
    protected String path;
    
    public Restore(String path){
        this.path=path;
    }
    @Override
    public void run(){
        for(int i = 0; i < Peer.getStorage().getStoredFiles().size(); i++){
            if(Peer.getStorage().getStoredFiles().get(i).getPath().equals(this.path)){
                String fileID=Peer.getStorage().getStoredFiles().get(i).getfileID();
                for(int n=0;n<Peer.getStorage().getStoredFiles().get(i).getNrChunks(); n++){
                    String header = "GETCHUNK " + Peer.getVersion() + " " + Peer.getId() + " " + fileID + " " + (n+1) + " " + Utility.CRLF + Utility.CRLF;
                    System.out.println("Sent " + header);
                    try{
                        Peer.getMC().message(header.getBytes("US-ASCII"));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
        System.out.println("File not found on this peer.");
    }
}