import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Multi-threaded server class to handle multiple outstanding client requests at once.
 * This class is able to handle requests from multiple running instances of
 * client doing concurrent PUT, GET, and DELETE operations.
 * This class is able to handle mutual exclusion.
 */
public class ServerImp extends UnicastRemoteObject implements ServerInterface, Runnable {
    private static Logger LOGGER = LogManager.getLogger(ServerImp.class.getName());

    private String requestType;
    private String key;
    private String value;

    private String returnVal;
    private Thread thread;

    public ServerImp() throws RemoteException {}

    public ServerImp(String requestType, String key, String value) throws RemoteException {
        this.requestType = requestType;
        this.key = key;
        this.value = value;
    }

    /**
     * Control the access of multiple threads to shared resource
     * @param key key from key-value store
     * @param value value from key-value store
     * @throws RemoteException
     */
    public synchronized void addToMap(String key, String value) throws RemoteException {
        LOGGER.info("Add KV store to map");
        MapHandler mapHandler = new MapHandler();
        HashMap<String, String> map = mapHandler.readMap();

        if (map != null) {
            LOGGER.info("MAP IS NOT EMPTY");
            mapHandler.writeToMap(key, value, map);
        } else {
            LOGGER.info("MAP IS EMPTY");
            HashMap<String, String> newMap = mapHandler.readMap();
            mapHandler.writeToMap(key, value, newMap);
        }

        LOGGER.info("Put successful");
    }

    /**
     * Create a new thread
     */
    public void start() {
        LOGGER.info("Create new thread");
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Process put request
     * @param key hashmap key
     * @param value hashmap value
     * @throws RemoteException
     */
    @Override
    public void put(String key, String value) throws RemoteException {
        LOGGER.info("Key is:" + key + " Value is:" + value);
        ServerImp serverThread = new ServerImp("PUT", key, value);
        serverThread.start();
    }

    /**
     * Process get request
     * @param key hashmap key
     * @return hashmap value
     * @throws RemoteException
     */
    @Override
    public String get(String key) throws RemoteException {
        LOGGER.info("key is: " + key);
        ServerImp serverThread = new ServerImp("GET", key, "");
        serverThread.start();
        return this.returnVal;
    }

    /**
     * Process delete request
     * @param key hashmap key
     * @throws RemoteException
     */
    @Override
    public void delete(String key) throws RemoteException {
        LOGGER.info("key is: " + key);
        ServerImp serverThread = new ServerImp("DELETE", key, "");
        serverThread.start();
    }

    @Override
    public void run() {
        LOGGER.info("Run New thread " + Thread.currentThread().getName() + " started");
        LOGGER.debug("requestType: " + requestType + " msgKey" + this.key);

        try {
            if (this.requestType != "" && this.requestType.equalsIgnoreCase("PUT")) {
                addToMap(this.key, this.value);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            ServerInterface serverObj = new ServerImp();
            // Change to dynamic!!
            Registry registry = LocateRegistry.createRegistry(8080);
            registry.bind("ServerImp", serverObj);
            LOGGER.info("Object bound successful");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
