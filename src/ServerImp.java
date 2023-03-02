import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
     * Mutual exclusion to control the access of multiple threads to shared resource.
     * Only one thread can execute at a critical session.
     * @param key key from key-value store
     * @param value value from key-value store
     */
    public synchronized void addToMap(String key, String value) throws IOException {
        MapHandler mapHandler = new MapHandler();
        HashMap<String, String> map = mapHandler.getMap();

        mapHandler.writeToMap(key, value);

        LOGGER.info("Current map size: " + map.size());
    }

    /**
     * Get the value of KV pair according to key
     * @param key key sent from client
     * @throws RemoteException error occurred in mapHandler
     */
    public void getFromMap(String key) throws RemoteException {
        MapHandler mapHandler = new MapHandler();
        HashMap<String, String> map = mapHandler.getMap();

        if (!map.isEmpty()) {
            if (map.containsKey(key)) {
                LOGGER.info("Retrieved KV store: " + "Key " + key + " " + "Value " + map.get(key));
            } else {
                String failureMsg = "Key-value pair not found";
                LOGGER.error(failureMsg);
            }
        } else {
            LOGGER.error("KV store is empty");
        }
    }

    public synchronized void deleteFromMap(String key) throws IOException {
        MapHandler mapHandler = new MapHandler();
        HashMap<String, String> map = mapHandler.getMap();

        if (!map.isEmpty()) {
            if (map.containsKey(key)) {
                mapHandler.deleteFromMap(key);
                LOGGER.info("Delete successful, new size after deletion: "+ map.size());
            } else {
                String failureMsg = "Key-value pair not found";
                LOGGER.error(failureMsg);
            }
        } else {
            LOGGER.error("KV store is empty");
        }
    }

    /**
     * Create a new thread
     */
    public void start() {
        LOGGER.info("New thread created");

        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Process put request
     * @param key hashmap key
     * @param value hashmap value
     * @throws RemoteException RPC call exception
     */
    @Override
    public void put(String key, String value) throws RemoteException {
        LOGGER.info("PUT request: Key is " + key + " Value is " + value);

        ServerImp serverThread = new ServerImp("PUT", key, value);
        serverThread.start();
    }

    /**
     * Process get request
     * @param key hashmap key
     * @return hashmap value
     * @throws RemoteException RPC call exception
     */
    @Override
    public String get(String key) throws RemoteException {
        LOGGER.info("GET request: Key is " + key);

        ServerImp serverThread = new ServerImp("GET", key, "");
        serverThread.start();
        return this.returnVal;
    }

    /**
     * Process delete request
     * @param key hashmap key
     * @throws RemoteException RPC call exception
     */
    @Override
    public void delete(String key) throws RemoteException {
        LOGGER.info("key is: " + key);
        ServerImp serverThread = new ServerImp("DELETE", key, "");
        serverThread.start();
    }

    @Override
    public void run() {
        LOGGER.info("Run new thread " + Thread.currentThread().getName());

        try {
            if (!this.requestType.equals("") && this.requestType.equalsIgnoreCase("PUT")) {
                addToMap(this.key, this.value);
            } else if (!this.requestType.equals("") && this.requestType.equalsIgnoreCase("GET")) {
                getFromMap(this.key);
            } else if (!this.requestType.equals("") && requestType.equalsIgnoreCase("DELETE")) {
                deleteFromMap(this.key);
            }
        } catch (IOException e) {
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
