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

    public static void main(String[] args) throws RemoteException, NotBoundException {
        // Change this part to dynamic input
        String hostName = "localhost";
        int portNum = 8080;
//
//        try {
            Registry registry = LocateRegistry.getRegistry(hostName, portNum);
            ServerInterface server = (ServerInterface) registry.lookup("ServerImp");
            LOGGER.info("Send put request");
            server.put("101", "1");
            LOGGER.info("Finished put request");
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }
}
