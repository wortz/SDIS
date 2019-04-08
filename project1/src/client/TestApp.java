package client;

import rmi.RmiInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp{
    private int peer_ap;
    private String sub_protocol;
    private String opnd_1;
    private String opnd_2;

    public TestApp(int peer_ap, String sub_protocol, String opnd_1, String opnd_2) {
        this.peer_ap = peer_ap;
        this.sub_protocol = sub_protocol;
        this.opnd_1 = opnd_1;
        this.opnd_2 = opnd_2;
    }


        public static void main(String args[]){

            if(args.length<4){
                System.out.println("Error: The format must be : java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
                return;
            }

            String host = null;
            String protocol=args[1];
            String file=args[2];
            int rep_deg=Integer.parseInt(args[3]);

            try {
                Registry registry = LocateRegistry.getRegistry(host);

                RmiInterface stub = (RmiInterface) registry.lookup("remote");
                stub.backupFile("penguin.gif", 1);
                System.out.println("response");
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
        }
    }
}