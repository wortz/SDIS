import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;


public class Peer{
    private BackupChannel MDB;

    private Peer(int type){
        byte[] msg = {'b','o'};
        MDB=new BackupChannel();
        if(type==1){
            MDB.message(msg);
        }
    }

    public BackupChannel getMDB(){
        return MDB;
    }
    public static void main(String args[]){
        Peer peer1= new Peer(Integer.parseInt(args[0]));

    }

}