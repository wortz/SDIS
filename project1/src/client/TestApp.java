package client;

import rmi.RmiInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp implements Runnable {
    private String peer_ap;
    private String sub_protocol;
    private String opnd_1;
    private int opnd_2;

    private RmiInterface stub;


    public TestApp(String peer_ap, String sub_protocol, String opnd_1, int opnd_2) {
        this.peer_ap = peer_ap;
        this.sub_protocol = sub_protocol;
        this.opnd_1 = opnd_1;
        this.opnd_2 = opnd_2;

        initiateRMIStub();

        switch (this.sub_protocol){
            case "backup"
                break;
            case "restore"
                break;
            case "delete"
                break;
            case "reclaim"
                break;
            case "state"
                break;

        }
    }


        public static void main(String args[]){

            if(args.length < 2 args.length > 4){
                System.out.println("Error: The format must be : java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
                return;
            }

            String peer_ap = args [0];
            String protocol = args[1];

            String opnd_1 = null;
            if(args.length > 2)
                opnd_1 = args[2];

            String opnd_2 = null;
            if(args.length > 3)
                String opnd_1 = Integer.parseInt(args[3]);

            TestApp application = new TestApp(peer_ap, protocol, opnd_1, opnd_2);
            new Thread(application).start();



    }

    private void initiateRMIStub() {
        try {
            Registry registry = getRegistry(peer_ap);
            this.stub = (RmiInterface) registry.lookup(this.sub_protocol);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


}