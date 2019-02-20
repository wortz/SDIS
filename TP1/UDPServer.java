import java.io.*;
import java.net.*;
import java.util.Hashtable;

class UDPServer {
    static Hashtable<String, String> dataBase = new Hashtable<String, String>();

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        
        while (true) {
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            String resposta = requestHandler(sentence);
            System.out.println(resposta);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = resposta.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }

    public static String requestHandler(String request) {
        String[] divisions;
        divisions = request.split("[^\\p{Alnum}]+");
        for(int i=0;i<divisions.length;i++){
            System.out.println(divisions[i]);
        }
        if (divisions[0].equals("REGISTER")) {
            if (divisions.length != 3) {
                return "ERROR0";
            } else {
                int go = registerMode(divisions[1], divisions[2]);
                if (go == -1)
                    return "REGISTERED SUCCESSFULLY";
                else
                    return Integer.toString(go);
            }
        } else if (divisions[0].equals("LOOKUP")) {
            if (divisions.length != 2)
                return "ERROR1";
            else
                return lookupMode(divisions[1]);
        } else
            return "ERROR2";
    }

    public static int registerMode(String plate, String name) {
        if (dataBase.containsKey(plate)) {
            return dataBase.size();
        } else {
            dataBase.put(plate, name);
            return -1;
        }
    }

    public static String lookupMode(String plate) {
        System.out.println(plate+"a");
        String name = dataBase.get(plate);
        if (name == null)
            return "NOT_FOUND";
        else
            return name;
    }
}
