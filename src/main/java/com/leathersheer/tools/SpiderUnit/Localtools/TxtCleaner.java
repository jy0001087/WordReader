package com.leathersheer.tools.SpiderUnit.Localtools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class TxtCleaner {
    public static final Logger cleanerLogger = LogManager.getLogger();

    public static void main(String[] args){
        String url = "D:\\MyFiles\\PersonalSync\\电子书\\非礼勿视的小说\\";
        TxtCleaner tcl =  new TxtCleaner();
        File[] files=tcl.getFileList(url);
        if(files==null){
            cleanerLogger.error("文件夹读取异常或没有文件夹");
        }
        int fileNum=tcl.getFileList(url).length;
        for (int i=0;i<fileNum;i++){
            tcl.fileIO(url,files[i]);
        }
    }

    public File[] getFileList(String url){
        File dir = new File(url);
        File[] files= dir.listFiles();
        if(files.length!=0){
            return files;
        }
        return null;
    }

    public void fileIO(String url,File file){

        if(!(file.getName().endsWith(".txt"))||file.getName().startsWith("clean-")){
            cleanerLogger.error("非txt文件或已转化");
            return;
        }
        try{
            File infile= new File(url+file.getName());
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(infile),"UTF-8");
            BufferedReader reader = new BufferedReader(streamReader);

            File resFile = new File(url+"clean-"+file.getName());
            BufferedWriter writer = new BufferedWriter(new FileWriter(resFile));

            String textline = null;

            while((textline=reader.readLine())!=null){
                if((textline=this.cleaner(textline))!= null){
                    cleanerLogger.trace(textline);
                    writer.write(textline);
                }
            }
            reader.close();
            writer.close();
        }catch(Exception e){
            cleanerLogger.error("读取文件异常",e);
        }
    }

    /**
     * 清理内容，删除多余空格及其它杂乱内容。
     * @param content
     * @return
     */
    public String cleaner(String content){
        String procString = content;
        if(procString.length()==0){  //如果是回车或其他无
            return null;
        }
        return procString+"\n";
    }
}
