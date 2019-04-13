package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.UnknownHostException;

public interface RmiInterface extends Remote {

    String test() throws RemoteException;

    void backupFile(String path, int replicationDegree) throws RemoteException;
    void restoreFile(String path) throws RemoteException;
    void deleteFile(String path) throws RemoteException;
}