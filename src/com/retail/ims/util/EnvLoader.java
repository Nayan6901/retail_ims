package com.retail.ims.util;

/**
 * Environment variable loader utility
 */
public class EnvLoader {
    
    /**
     * Load environment variables from .env file
     * @param filename .env file path
     * @return true if loaded successfully
     */
    public static boolean loadEnv(String filename) {
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(filename);
            java.util.Properties props = new java.util.Properties();
            props.load(fis);
            
            // Set system properties
            for (String key : props.stringPropertyNames()) {
                System.setProperty(key, props.getProperty(key));
            }
            
            fis.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            return false;
        }
    }
}