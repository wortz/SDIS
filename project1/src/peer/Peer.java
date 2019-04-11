package peer;

import channels.*;
import rmi.RmiInterface;
import utility.Utility;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class Peer implements RmiInterface {
    private static double version;

    private String id;
    private String service_ap;

    private Channel MC;
    private Channel MDB;
    private Channel MDR;

    private ScheduledExecutorService exec;

    private Peer(String args[]) throws IOException {
        exec = new ScheduledThreadPoolExecutor(10);

        this.version = Double.parseDouble(args[0]);

        this.id = args[1];

        this.service_ap = args[2];
        this.initRmi(service_ap);

        String MC_Address = args[3];
        int MC_Port = Integer.parseInt(args[4]);

        String MDB_Address = args[5];
        int MDB_Port = Integer.parseInt(args[6]);

        String MDR_Address = args[7];
        int MDR_Port = Integer.parseInt(args[8]);

        MC = new Channel(MC_Address, MC_Port,this);
        MDB = new Channel(MDB_Address, MDB_Port,this);
        MDR = new Channel(MDR_Address, MDR_Port,this);

        //exec.execute(MC);
        exec.execute(MDB);
        //exec.execute(MDR);
    }

    public String getId() {
        return id;
    }

    @Override
    public String test() {
        System.out.println("Testing RMI");
        return "Testing RMI";
    }

    @Override
    public void backupFile(String path, int replicationDegree) throws RemoteException {
        File file = new File(path);
        String fileID = Utility.getFileSHA(file);
        try {
            String headerAux = "PUTCHUNK " + this.version + " " + this.id + " " + fileID + " ";
            ArrayList<byte[]> chunks = Utility.getChunks(path);
            System.out.println(chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                String restMessage = headerAux + (i + 1) + " " + replicationDegree + " " + Message.CRLF + Message.CRLF;
                byte[] body = chunks.get(i);
                byte[] header = restMessage.getBytes("US-ASCII");
                byte[] message = new byte[header.length+body.length];
                System.arraycopy(header, 0, message, 0, header.length);
                System.arraycopy(body, 0, message, header.length, body.length);
                Message msg=new Message(message, this.MDB);
                exec.execute(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivedMessage(DatagramPacket packet){
        int packet_length=packet.getLength();
        byte[] msg= Arrays.copyOfRange(packet.getData(), 0, packet_length);
        MessageHandler handler=new MessageHandler(msg,this);
        exec.execute(handler);
    }


    public void handlePutChunk(){
        String message = "Stored 1.0 1 1 " + Message.CRLF + Message.CRLF;
        try{
        byte[] buf = message.getBytes("US-ASCII");
        Message control = new Message(buf,this.MC);
        exec.execute(control);
        }   catch(IOException e){
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

        if (args.length < 9) {
            System.out.println(
                    "Insert arguments as <protocol_version> <server_id> <service_ap> <MC_Address> <MC_Port> <MDB_Address> <MDB_Port> <MDR_Address> <MDR_Port> ");
        }

        Peer peer = new Peer(args);
    }
}