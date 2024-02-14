package parssist.parser.top_down_analysis.nrdparser.parser.util.tree.visitor;

import java.util.List;
import java.util.Map;


/**
 * JSON like object.
 * Because of webassembly, the visitor can't use the org.json library. 
 * It uses the D3.js JSON Tree notation.
 */
public class JsonObject {
    /**
     * Converts a map to a JSON like string.
     * @param map The map to convert.
     * @return The JSON like string.
     */
    public static String mapToJsonString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("\"name\": \"" + entry.getKey() + "\",");
            sb.append("\"children\":");
            if(entry.getValue() instanceof String) {
                sb.append("\"" + entry.getValue() + "\"");
            } else if(entry.getValue() instanceof List) {
                sb.append(listToJsonString((List) entry.getValue()));
            } else if(entry.getValue() instanceof Map) {
                sb.append(mapToJsonString((Map) entry.getValue()));
            }
            sb.append(",");
        }

        if(sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts a list to a JSON like string.
     * @param list The list to convert.
     * @return The JSON like string.
     */
    public static String listToJsonString(List<Object> list) {
        StringBuilder jsonString = new StringBuilder("[");

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                jsonString.append(",");
            }

            Object value = list.get(i);
            if (value instanceof Map) {
                jsonString.append(mapToJsonString((Map<String, Object>) value));
            } else if (value instanceof List) {
                jsonString.append(listToJsonString((List<Object>) value));
            } else {
                jsonString.append(value);
            }
        }

        jsonString.append("]");
        return jsonString.toString();
    }
}
