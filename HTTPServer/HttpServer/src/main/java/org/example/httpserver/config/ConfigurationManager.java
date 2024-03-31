package org.example.httpserver.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpsConfigurator;
import org.example.httpserver.util.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager myconfigurationManager;
    public static Configuration myCurrentConfiguration;

     private ConfigurationManager(){
     }

     public static ConfigurationManager getInstance(){

         if(myconfigurationManager == null)                       /**You're checking if the configuration manager exists and if not, you create one */
             myconfigurationManager = new ConfigurationManager();
         return myconfigurationManager;
     }

     /**
        Used to load a configuration file by the path provided
      */
     public void loadConfigurationManager(String filePath) throws IOException {
         FileReader fileReader = null;
         try{
             fileReader = new FileReader(filePath);
         }catch (FileNotFoundException e){
             throw new HttpConfigurationException();
         }
         //FileReader fileReader = new FileReader(filePath);
         StringBuffer sb= new StringBuffer();
         int i;
         try{
             while((i= fileReader.read()) != -1){
                 sb.append((char)i);
             }
         } catch(IOException e){
             throw new HttpConfigurationException();
         }
         JsonNode conf = null;
         try{
             conf = Json.parse(sb.toString());
         } catch(IOException e){
             throw new HttpConfigurationException("Error parsing the configuration the configuration file", e);
         }
         try{
             myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
         } catch(IOException e){
             throw new HttpConfigurationException("Error parsing the Configuration file, internal", e);
         }

     }

     /**
        Returns the current loaded configuration
      */
     public Configuration getCurrentConfiguration(){
        if(myCurrentConfiguration == null){
            throw new HttpConfigurationException("No Current Configuration set");
        }
        return myCurrentConfiguration;
     }

}
