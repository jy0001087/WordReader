package com.leathersheer.tools.SpiderUnit.PubToolUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PropertiesReader {
    private ServletContext context;
    public static final Logger propertyReaderLogger = LogManager.getLogger();

    public PropertiesReader(ServletContext context){
        this.context=context;
    }

    public HashMap<String,String> getProperties(String filename, ArrayList<String> propertiesPath){
        HashMap<String,String> resMap= new HashMap<>();
        JSONObject json = this.getPropertiesJson(filename);
        for(String path:propertiesPath){
            String result="";
            String resultKey="";
            String[] paths=path.split("\\|");
            for(int i=0;i<paths.length;i++){
                if(i+1==paths.length){
                    result=json.getString(paths[i]);
                    resultKey=paths[i];
                }else{
                    json=json.getJSONObject(paths[i]);
                }
            }
            resMap.put(resultKey,result);
        }
        return resMap;
    }
    public JSONObject getPropertiesJson(String filename) {
        JSONObject json = null;
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
