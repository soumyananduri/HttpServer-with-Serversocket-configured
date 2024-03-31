package org.example;


import com.sun.net.httpserver.HttpServer;
import org.example.httpserver.config.Configuration;
import org.example.httpserver.config.ConfigurationManager;
import org.example.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.logging.Logger;

@SpringBootApplication
public class SimpleHTTPServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    public static void main(String[] args) throws Exception{

        LOGGER.info("Server starting");
        System.out.println("Server starting on port 8080.......");

        ConfigurationManager.getInstance().loadConfigurationManager("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using port: "+ conf.getPort());
        LOGGER.info("Using WebRoot: "+ conf.getWebroot());

        try{
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch(IOException e){
            e.printStackTrace();
            // TODO Please Handle later
        }

        /* //System.out.println("Hello world!");
        final ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on Port 8080......");
        SpringApplication.run(SimpleHTTPServer.class, args);
        while(true){
            //spin forever
        }
         */





    }
}