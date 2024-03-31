package org.example.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.xmlunit.builder.Input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;
    public HttpConnectionWorkerThread(Socket socket) throws InterruptedException {
        this.socket= socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream= null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            int _byte= inputStream.read();
            while((_byte = inputStream.read()) >=0){
                System.out.print((char)_byte);
            }

            //TODO we would write
            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>This page was served  using my Simple Java HTTP server." +
                    "</h1></body></html>";
            final String CRLF = "\n\r"; //13,10 (ASCII)
            String response =
                    "HTTP?1.1 200 OK"+ CRLF + //Status Line: HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: "+ html.getBytes().length + CRLF + //HEADER
                            CRLF+
                            html+
                            CRLF+CRLF;
            outputStream.write(response.getBytes());
            //TODO we would read - Not going to read from the browser so not required here

//            inputStream.close();
//            outputStream.close();
            //serverSocket.close();  //TODO handle later
//            try{
//                sleep(5000);
//            } catch(InterruptedException e){
//                e.printStackTrace();
//            }
            LOGGER.info("Connection Processing Finished");
        } catch(IOException e){
            LOGGER.info("Problem with communication", e);
            e.printStackTrace();
        }finally{
            if (inputStream != null) {

                try{
                    inputStream.close();
                }catch(IOException e){}
            }
            if(outputStream!=null){
                try{
                    outputStream.close();
                }catch(IOException e){}
            }
            if(socket!=null){
                try{
                    socket.close();
                }catch(IOException e){}
            }
        }

    }
}
