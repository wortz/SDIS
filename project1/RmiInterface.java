import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.UnknownHostException;

public interface RmiInterface extends Remote {
    void sendMessage() throws RemoteException, UnknownHostException, InterruptedException;
    //public void backup(String filepath, int replicationDegree) throws RemoteException;        
    //public void restore(String filepath) throws RemoteException;
    //public void delete(String filepath) throws RemoteException;
}