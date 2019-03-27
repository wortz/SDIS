



public class TestApp{


    private TestApp(){

    }


    public static void main(String args[]){
        if(args.length<4){
            System.out.println("Error: The format must be : java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
        }

        String peerId = Integer.parseInt(args[0]);
        String protocol=args[1];
        String file=args[2];
        int rep_deg=args[3];

        

    }
}