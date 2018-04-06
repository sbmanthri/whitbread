package com.test;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

public class Configuration {
    private Configuration(){

    }

    public static  Properties properties;

    public static void load() throws  IOException{
        Configuration config = new Configuration();
        properties = new Properties();
       Enumeration<URL> enumerator;
        enumerator = config.getClass().getClassLoader().getResources( "/");
        while(enumerator.hasMoreElements()){
            System.out.println(enumerator.nextElement());
        }

        try (InputStream is = Files.newInputStream(Paths.get("config.properties"))){

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            try {
                properties.load(reader);

            }finally {
                is.close();
                reader.close();
            }
        }
    }

    public static String get(String option) {
        if(properties == null){
            try {
                load();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        String value = properties.getProperty(option);
        if(value == null){
            return "";
        }
        return value;
    }
}
