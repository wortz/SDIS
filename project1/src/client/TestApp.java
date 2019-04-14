package client;

import rmi.RmiInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp {
    public static String host = null;
    public static int port = 8080;

    public static void main(String args[]) {

        if (args.length < 2 || args.length > 4) {
            System.out.println("Error: The format must be : java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
            return;
        }
        String peer_ap = args[0];
        String sub_protocol = args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(host,0);
            RmiInterface stub = (RmiInterface) registry.lookup(peer_ap);
            switch (sub_protocol) {
                case "BACKUP":
                    //java TestApp 1923 BACKUP test1.pdf 3
                    String pathBackup = args[2];
                    int replicationDegree = Integer.parseInt(args[3]);
                    stub.backupFile(pathBackup, replicationDegree);
                    break;
                case "RESTORE":
                    //java TestApp 1923 RESTORE test1.pdf
                    String pathRestore = args[2];
                    stub.restoreFile(pathRestore);
                    break;
                case "DELETE":
                    //java TestApp 1923 DELETE test1.pdf
                    String pathDelete = args[2];
                    stub.deleteFile(pathDelete);
                    break;
                case "RECLAIM":
                    //java TestApp 1923 RECLAIM 0
                    int sizeReclaim = Integer.parseInt(args[2]);
                    stub.reclaim(sizeReclaim);
                    break;
                case "STATE":
                    break;
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}