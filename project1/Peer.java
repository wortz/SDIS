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
        MDB=new BackupChannel();
    }

    public BackupChannel getMDB(){
        return MDB;
    }
    public static void main(String args[]){
        Peer peer1= new Peer(1);

    }

}