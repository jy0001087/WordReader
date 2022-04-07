package com.leathersheer.tools.SpiderUnit.Servlets;

import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/ChengDuHouse",name = "ChengDuHouse")
public class ChengDuHouseServlet extends HttpServlet {
        public static final Logger CDListnerLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CDListnerLogger.info("CDListner start!");
        new ChengDuHouseStatement().doGrab("https://www.cdzjryb.com/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CDListnerLogger.info("CDListner start!");
        new ChengDuHouseStatement().doGrab("https://www.cdzjryb.com/");
    }

}
