import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropsHandler {
    private final Properties props = new Properties();
    private final Properties propsServer = new Properties();
    String outputPath = "/Users/jasmine/IdeaProjects/CS6650-Project2/src/map.properties";

    /**
     * Read in pre-populated data set
     */
    private PropsHandler() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("rpc.properties");
        InputStream inServer = this.getClass().getClassLoader().getResourceAsStream("map.properties");

        try {
            props.load(in);
            propsServer.load((inServer));
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

    public String getPropertiesServer(String key) {
        return propsServer.getProperty(key);
    }

    /**
     * Get all the properties
     * @return all the values
     */
    public Set<String> getAllValues() {
        return propsServer.stringPropertyNames();
    }

    /**
     * Get whether there is existing key-value pair
     * @param key key of a key-value pair
     * @return whether there is existing key-value pair
     */
    public boolean containsKey(String key) {
        return props.containsKey(key);
    }

    public void setPropsServer(String key, String value) {
        propsServer.setProperty(key, value);
    }

    public void writeToFile() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        propsServer.store(outputStream, null);
    }

    public void deleteFromFile(String key) throws IOException {
        propsServer.remove(key);
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        propsServer.store(outputStream, null);
    }
}
