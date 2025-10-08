package com.retail.ims.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple JSON utility class (replacing Gson for simplicity)
 */
public class JsonUtil {
    
    /**
     * Convert Map to JSON string
     * @param map Map to convert
     * @return JSON string
     */
    public static String toJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(entry.getKey()).append("\":");
            json.append(formatValue(entry.getValue()));
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Format value based on its type
     * @param value Value to format
     * @return Formatted string
     */
    private static String formatValue(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeString((String) value) + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Map) {
            return toJson((Map<String, Object>) value);
        } else {
            return "\"" + escapeString(value.toString()) + "\"";
        }
    }
    
    /**
     * Escape special characters in string
     * @param str String to escape
     * @return Escaped string
     */
    private static String escapeString(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}