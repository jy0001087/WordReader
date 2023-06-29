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

@WebServlet(urlPatterns = "/shuaigayServlet",name = "shuaigayServlet")
public class ShuaiGayServlet extends HttpServlet {
    public static final  Logger shuaigayfetcherServletLogger = LogManager.getLogger();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        shuaigayfetcherServletLogger.info("ShuaiGayFetcher have been started with para:"+req.getParameter("threadId"));
        ShuaigayFetcher fetcher = new ShuaigayFetcher();
        ServletConfig config=this.getServletConfig();
        fetcher.doArticleGrab(req.getParameter("threadId").toString(),this.getServletContext());
        req.setAttribute("Article","ok了");
        req.getRequestDispatcher("AmenityServlet").forward(req,resp);
    }
}
