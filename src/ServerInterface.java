import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    /**
     * Process put request
     * @param key hashmap key
     * @param value hashmap value
     * @throws RemoteException
     */
    void put(String key, String value) throws RemoteException;

    /**
     * Process get request
     * @param key
     * @return
     * @throws RemoteException
     */
    String get(String key) throws RemoteException;

    /**
     *
     * @param key
     * @throws RemoteException
     */
    void delete(String key) throws RemoteException;
}
