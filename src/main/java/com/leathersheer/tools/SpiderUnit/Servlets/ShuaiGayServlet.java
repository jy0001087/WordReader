package com.leathersheer.tools.SpiderUnit.Servlets;

import com.leathersheer.tools.SpiderUnit.Shuaigay.ShuaigayFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/shuaigayServlet",name = "shuaigayServlet")
public class ShuaiGayServlet extends HttpServlet {
    public static final  Logger shuaigayfetcherServletLogger = LogManager.getLogger();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("threadId");  //前台就不改了 threadID改为整条url
        shuaigayfetcherServletLogger.info("ShuaiGayFetcher has started with para:"+url);
        ShuaigayFetcher fetcher = new ShuaigayFetcher();
        fetcher.doArticleGrab(getThreadId(url),this.getServletContext());
        req.setAttribute("Article","ok了");
        req.getRequestDispatcher("Amenity").forward(req,resp);
    }

    public String getThreadId(String url){
        String threadId="";
        Pattern p = Pattern.compile("tid=\\d+");
        Matcher m = p.matcher(url);
        if(m.find()){
            Pattern innerP = Pattern.compile("\\d+");
            Matcher innerM = innerP.matcher(m.group());
            if (innerM.find()) {
                threadId = innerM.group();
            }
        }
        return threadId;
    }
}
