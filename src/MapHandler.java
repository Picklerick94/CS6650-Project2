import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapHandler {
    private static Logger LOGGER = LogManager.getLogger(MapHandler.class.getName());
    HashMap<String, String> map = new HashMap<>();

    /**
     * Read from pre-populated properties and create key-value stores
     * @return key-value stores
     */
    public HashMap<String, String> getMap() {
        int len = PropsHandler.getInstance().getAllValues().size();
        Object[] arr = PropsHandler.getInstance().getAllValues().toArray();

        if (len > 0) {
            for (int i = 0; i < len; i++) {
                String key = (String) arr[i];
                map.put(key, PropsHandler.getInstance().getPropertiesServer(key));
            }
        }

        return map;
    }

    /**
     * Put new key-value pair in map
     * @param key key in key-value store
     * @param value value in key-value store
     */
    public void writeToMap(String key, String value) throws IOException {
        map.put(key, value);
        PropsHandler.getInstance().setPropsServer(key, value);
        PropsHandler.getInstance().writeToFile();
    }

    public void deleteFromMap(String key) throws IOException {
        map.remove(key);
        PropsHandler.getInstance().deleteFromFile(key);
    }
}
