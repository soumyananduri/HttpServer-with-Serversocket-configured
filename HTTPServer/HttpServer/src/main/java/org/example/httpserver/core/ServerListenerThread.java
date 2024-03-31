package org.example.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * If we want our server to have different sockets function at the same time
 * Binding out sockets to different port number or IP addresses at the same time using multi-threading
 */
public class ServerListenerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        /**
         Binding the server socket to your port number
         */

        try {
            /**
             * ServerSocket- Waits for connection
             * Socket- Initiates the accepted connection
             */
            //ServerSocket serverSocket = new ServerSocket(conf.getPort());
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                LOGGER.info(" * Connection Accepted: " + socket.getInetAddress());

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }
            // serverSocket.close(); // TODO Handle later

        } catch (IOException | InterruptedException e) {
            //throw new RuntimeException(e);
            LOGGER.info("Problem with setting socket", e);
        }finally{
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
