import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Peer{
    private BackupChannel MDB;

    private Peer(){
        MDB=new BackupChannel();
    }
    public static void main(String args[]){
        Peer peer= new Peer();
    }
}