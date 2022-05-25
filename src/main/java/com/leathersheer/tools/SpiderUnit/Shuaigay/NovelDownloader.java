package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/NovelDownload",name = "NovelDownload")
public class NovelDownloader extends HttpServlet {
    public static final Logger novelDownloadLogger = LogManager.getLogger();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        novelDownloadLogger.info("Enter NovelDownload!");

        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String fileListArrayStringValue = mapper.writeValueAsString("No file found");
        out.print(fileListArrayStringValue);
    }
}
