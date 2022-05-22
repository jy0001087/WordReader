package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/ShuaigayNovelContent",name = "ShuaigayNovelContent")
public class ShuaigayNovelContent extends HttpServlet {
    public final static Logger SNCLogger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        ServletConfig config = this.getServletConfig();
        String path = config.getServletContext().getRealPath("/") + "NovelDir";
        ArrayList<File> fileList = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        PrintWriter out = null;
        try {
            resp.setCharacterEncoding("UTF-8");
            out = resp.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String fileListArrayStringValue = mapper.writeValueAsString("No file found");
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    fileList.add(files[i]);
                }
                fileListArrayStringValue = mapper.writeValueAsString(fileList);
            }
            out.print(fileListArrayStringValue);
        } catch (Exception e) {
            SNCLogger.error(e);
        }

    }
}
