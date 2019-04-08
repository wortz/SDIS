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
import rmi.RmiStub;


public class Peer extends RmiStub{
    private static double version;

    private static int id;
    private String rmiAccess;
/*
    private ControlChannel MC;
    private BackupChannel MDB;
    private RestoreChannel MDR;
    */
    
    private Peer(String args[]){
        this.version = Double.parseDouble(args[0]);

        this.id = Integer.parseInt(args[1]);

        this.rmiAccess = args[2];
        this.initRmiStub(rmiAccess);
/*
        String MC = args[3];
        int portMC = Integer.parseInt(args[4]);

        String MDB = args[5];
        int portMDB = Integer.parseInt(args[6]);

        String MDR = args[7];
        int portMDR = Integer.parseInt(args[8]);


        new Thread(MC).start();
        new Thread(MDB).start();
        new Thread(MDR).start();

        try{
            MC = new ControlChannel(MC, portMC);
            MDB = new BackupChannel(MDB, portMDB);
            MDR = new RestoreChannel(MDR, portMDR);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        */
    }

    public static void main(String args[]){
        
        if(args.length < 9){
            System.out.println("Insert arguments as <protocol version> <server id> <service access point> <MC address> <MC port> <MDB address> <MDB port> <MDR address> <MDR port> ");
        }

        Peer peer = new Peer(args);
    }


}