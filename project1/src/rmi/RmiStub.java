package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiStub implements RmiInterface {

    @Override
    public String test() {
        System.out.println("Testing RMI");
        return "Testing RMI";
    }

    protected void initRmiStub(String rmiAccess) {
        try {
            RmiStub obj = new RmiStub();
            RmiInterface stub = (RmiInterface) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(rmiAccess, stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}