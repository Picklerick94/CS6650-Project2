import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Client class using RMI for RPC communication
 */
public class Client {
    private static Logger LOGGER = LogManager.getLogger(Client.class.getName());
    // Change this part to dynamic input!!
    private static String hostName = "localhost";
    private static int portNum = 8080;

    private static void put(Registry registry) throws RemoteException, NotBoundException {
        ServerInterface server = (ServerInterface) registry.lookup("ServerImp");
        // Read in pre-populated data
        String putData = PropsHandler.getInstance().getProperties("PUT_REQUEST_DATA");
        String[] items = putData.split("\\s*\\|\\s*");

        // 5 PUT requests
        for (String item : items) {
            String key = item.substring(0, item.indexOf(","));
            String value = item.substring(item.indexOf(",") + 1);
            server.put(key, value);
        }
    }

    public static void get(Registry registry) throws RemoteException, NotBoundException {
        ServerInterface server = (ServerInterface) registry.lookup("ServerImp");
        // Read in pre-populated data
        String getData = PropsHandler.getInstance().getProperties("GET_REQUEST_DATA");
        String[] items = getData.split("\\s*,\\s*");

        for (String item : items) {
            server.get(item);
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostName, portNum);
//            put(registry);
            get(registry);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
