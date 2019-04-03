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


public class Peer implements RmiInterface{
    private static double version;
    private static int peerID;

    private ControlChannel MC;
    //private BackupChannel MDB;
    //private RestoreChannel MDR;
    
    
    private Peer(String MC, int portMC, String MDB, int portMDB, String MDR, int portMDR){
        try{
            //MC = new ControlChannel(MC, portMC);
            //MDB = new BackupChannel(MDB, portMDB);
            //MDR = new RestoreChannel(MDR, portMDR);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        
        if(args.length < 6){
            System.out.println("Insert arguments as <protocol version> <id peer> <service access point> <IP MC> <Port MC> <IP MDB> <Port MDB> <IP MDR> <Port MDR> ");
        }

        version = Double.parseDouble(args[0]);
        peerId = Integer.parseInt(args[1]);

        String acessPoint = args[2];

        String MC = args[3];
        int portMC = Integer.parseInt(args[4]);
        String MDB = args[5];
        int portMDB = Integer.parseInt(args[6]);
        String MDR = args[7];
        int portMDR = Integer.parseInt(args[8]); 

        Peer peer = new Peer(MC, portMC, MDB, portMDB, MDR, portMDR);
    }

    public BackupChannel getMDB(){
        return MDB;
    }

}