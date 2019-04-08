package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.UnknownHostException;

public interface RmiInterface extends Remote {
    String test() throws RemoteException;
}