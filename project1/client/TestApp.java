package client;

import rmi.RmiInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp{


    public static void main(String args[]){
        if(args.length<4){
            System.out.println("Error: The format must be : java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
        }

        String host = null;
        String protocol=args[1];
        String file=args[2];
        int rep_deg=Integer.parseInt(args[3]);

        try {
            Registry registry = LocateRegistry.getRegistry(host);

            RmiInterface stub = (RmiInterface) registry.lookup("remote");
            String response = stub.test();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        

    }
}