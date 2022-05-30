package com.leathersheer.tools.SpiderUnit.PubToolUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PropertiesReader {
    private final ServletContext context;
    public static final Logger propertyReaderLogger = LogManager.getLogger();
    private String filename;
    private JSONObject json;
    private ArrayList<String> propertiesPath;
    private HashMap<String,String> resMap;

    private PropertiesReader(Builder builder){
        this.context=builder.context;
        this.filename=builder.filename;
        this.propertiesPath=builder.propertiesPath;

        this.resMap=new HashMap<>();
        this.json = getPropertiesJson();
        this.getProperties();
    }

    public static class Builder{
        private final ServletContext context;
        private final String filename;
        private  ArrayList<String> propertiesPath;

        public Builder(ServletContext context,String filename){
            this.context=context;
            this.filename=filename;
            this.propertiesPath=new ArrayList<>();
        }

        public Builder setPropertyPath(String path){
            this.propertiesPath.add(path);
            return this;
        }

        public PropertiesReader build(){
            return new PropertiesReader(this);
        }
    }


    public HashMap<String,String> getProperties(){
        for(String path:propertiesPath){
            String result="";
            String resultKey="";
            JSONObject processJson = json;
            String[] paths=path.split("\\|");
            for(int i=0;i<paths.length;i++){
                if(i+1==paths.length){
                    result=processJson.getString(paths[i]);
                    resultKey=paths[i];
                }else{
                    processJson=processJson.getJSONObject(paths[i]);
                }
            }
            resMap.put(resultKey,result);
        }
        return resMap;
    }

    public String getProperty(String propertyName){
        String propertyValue=resMap.get(propertyName);
        return propertyValue;
    }

    public JSONObject getPropertiesJson() {
        String path = context.getRealPath("/") + "WEB-INF" + File.separator + "classes" + File.separator + "properties" + File.separator + filename;
        File propertiesFile = new File(path);
        String jsonString = "";
        FileInputStream filein=null;
        BufferedReader reader = null;
        try {
            if (propertiesFile.isFile()) {
                 filein = new FileInputStream(propertiesFile);
                 reader = new BufferedReader(new InputStreamReader(filein));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonString = jsonString + line;
                }
            }
            json = new JSONObject(jsonString);
        } catch (Exception e) {
            propertyReaderLogger.error("File read error:", e);
        }finally {
            try {
                reader.close();
                filein.close();
            }catch(Exception e){
                propertyReaderLogger.error("File close failed:",e);
            }
        }
        return json;
    }
}
