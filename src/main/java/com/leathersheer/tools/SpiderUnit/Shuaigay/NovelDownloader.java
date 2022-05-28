package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

@WebServlet(urlPatterns = "/NovelDownload",name = "NovelDownload")
public class NovelDownloader extends HttpServlet {
    public static final Logger novelDownloadLogger = LogManager.getLogger();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        novelDownloadLogger.info("Enter NovelDownload!");

        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type","text/plain");
        resp.setHeader("content-disposition", "attachment;filename=1.txt");
        ServletOutputStream out = resp.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        String fileListArrayStringValue = mapper.writeValueAsString("File is not found");

        ServletConfig config = this.getServletConfig();
        String path = config.getServletContext().getRealPath("/") + "NovelDir"+File.separator+ URLDecoder.decode(req.getParameter("filename"),"UTF-8");
        novelDownloadLogger.debug("file path="+path);
        File file= new File(path);
        if(file.isFile()){
            try {
                FileInputStream fs = new FileInputStream(file);
                int length= -1;
                byte[] bytes=new byte[1024];
                while((length=fs.read(bytes))!=-1){
                    out.write(bytes,0,length);
                }
            }catch(Exception e){
                novelDownloadLogger.error("NovelReader error",e);
            }
        }else{
            out.print(fileListArrayStringValue);
        }


    }

    public void NovelReader(String url){

    }
}
