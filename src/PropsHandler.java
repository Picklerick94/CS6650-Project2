import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropsHandler {
    private final Properties props = new Properties();

    /**
     * Read in pre-populated data set
     */
    private PropsHandler() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("rpc.properties");

        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new RpcHelper instance
     */
    private static class InstanceInit {
        private static final PropsHandler INSTANCE = new PropsHandler();
    }

    /**
     * Return an instance of client.PropertiesHandler
     * @return instance of client.PropertiesHandler
     */
    public static PropsHandler getInstance() {
        return InstanceInit.INSTANCE;
    }

    /**
     * Get the value according to a key
     * @param key key of a key-value pair
     * @return value of a key value pair
     */
    public String getProperties(String key) {
        return props.getProperty(key);
    }

    /**
     * Get all the properties
     * @return all the values
     */
    public Set<String> getAllValues() {
        return props.stringPropertyNames();
    }

    /**
     * Get whether there is existing key-value pair
     * @param key key of a key-value pair
     * @return whether there is existing key-value pair
     */
    public boolean containsKey(String key) {
        return props.containsKey(key);
    }
}
