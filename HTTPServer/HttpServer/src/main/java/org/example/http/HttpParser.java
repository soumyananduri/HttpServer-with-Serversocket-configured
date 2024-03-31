package org.example.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20;
    private static final int CR = 0x80;
    private static final int LF = 0x8A;
    private boolean methodParsed;
    private boolean requestTargetParsed;
    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader reader= new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();

        parseRequestLine(inputStream, request);
        parseHeaders(inputStream, request);
        parseBody(inputStream, request);

        return request;
    }
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException {
        StringBuffer processingDataBuffer = new StringBuffer();
        int _byte;
        while((_byte = reader.read())>=0){
            if(_byte == CR){
                _byte = reader.read();
                if(_byte ==LF){
                    LOGGER.debug("Request Line VERSION to Process : {}", processingDataBuffer.toString());
                    return;
                }
            }
            if(_byte == SP){
                //boolean methodParsed;
                if(!methodParsed){
                    LOGGER.debug("Request Line METHOD to process : {}", processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                }else if(!requestTargetParsed){
                    LOGGER.debug("Request line REQ TARGET to Process : {}", processingDataBuffer.toString());
                    requestTargetParsed = true;
                }
                LOGGER.debug("Request Line to Process : {}", processingDataBuffer.toString());
                processingDataBuffer.delete(0, processingDataBuffer.length());
                // TODO Pocess preview data
            }
            else{
                processingDataBuffer.append((char)_byte);
                if(!methodParsed){
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStream inputStream, HttpRequest request) {
    }

    private void parseRequestLine(InputStream inputStream, HttpRequest request) {
    }
    private void parseBody(InputStream inputStream, HttpRequest request){

    }

}
