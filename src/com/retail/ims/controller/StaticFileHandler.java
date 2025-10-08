package com.retail.ims.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Static file handler for serving HTML, CSS, JS files
 */
public class StaticFileHandler implements HttpHandler {
    
    private final String rootDirectory;
    
    public StaticFileHandler(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        // Default to index.html for root path
        if ("/".equals(path)) {
            path = "/index.html";
        }
        
        File file = new File(rootDirectory + path);
        
        if (file.exists() && file.isFile()) {
            // Set content type based on file extension
            String contentType = getContentType(path);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            
            // Send file
            exchange.sendResponseHeaders(200, file.length());
            OutputStream os = exchange.getResponseBody();
            FileInputStream fis = new FileInputStream(file);
            
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            
            fis.close();
            os.close();
        } else {
            // File not found
            String response = "404 - File Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "text/plain";
    }
}