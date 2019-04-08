import channels.*;
import rmi.RmiInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Peer implements RmiInterface {
    private static double version;

    private static int id;
    private String service_ap;

    private ControlChannel MC;
    private BackupChannel MDB;
    private RestoreChannel MDR;
    
    
    private Peer(String args[]) throws IOException {
        this.version = Double.parseDouble(args[0]);

        this.id = Integer.parseInt(args[1]);

        this.service_ap = args[2];
        this.initRmi(service_ap);

        String MC_Address = args[3];
        int MC_Port = Integer.parseInt(args[4]);

        String MDB_Address = args[5];
        int MDB_Port = Integer.parseInt(args[6]);

        String MDR_Address = args[7];
        int MDR_Port = Integer.parseInt(args[8]);

        MC = new ControlChannel(MC_Address, MC_Port);
        MDB = new BackupChannel(MDB_Address, MDB_Port);
        MDR = new RestoreChannel(MDR_Address, MDR_Port);


        new Thread(MC).start();
        new Thread(MDB).start();
        new Thread(MDR).start();

    }

    @Override
    public String test() {
        System.out.println("Testing RMI");
        return "Testing RMI";
    }

    @Override
    public void backupFile(String filePath, int replicationDegree) throws RemoteException{
        String header = "PUTCHUNK 1.0 " + this.id + " FileID 1 1 " + Message.CRLF + Message.CRLF;
        try {
            MDB.message(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initRmi(String service_ap) {
        try {
            RmiInterface stub = (RmiInterface) UnicastRemoteObject.exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(service_ap, stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        
        if(args.length < 9){
            System.out.println("Insert arguments as <protocol_version> <server_id> <service_ap> <MC_Address> <MC_Port> <MDB_Address> <MDB_Port> <MDR_Address> <MDR_Port> ");
        }

        Peer peer = new Peer(args);
    }
}