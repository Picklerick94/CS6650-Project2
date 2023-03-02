import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MapHandler {
    private static Logger LOGGER = LogManager.getLogger(MapHandler.class.getName());

    /**
     * Read from pre-populated properties and create key-value stores
     * @return key-value stores
     */
    public HashMap<String, String> readMap() {
        String putData = PropsHandler.getInstance().getProperties("PUT_REQUEST_DATA");
        String[] items = putData.split("\\s*\\|\\s*");
        HashMap<String, String> maps = new HashMap<>();;

        for (String item : items) {
            String key = item.substring(0, item.indexOf(","));
            String value = item.substring(item.indexOf(","));
            maps.put(key, value);
        }

        return maps;
    }

    /**
     * Put new key-value pair in map
     * @param key key in key-value store
     * @param value value in key-value store
     * @param map
     */
    public void writeToMap(String key, String value, HashMap<String, String> map) {
        map.put(key, value);

    }
}
