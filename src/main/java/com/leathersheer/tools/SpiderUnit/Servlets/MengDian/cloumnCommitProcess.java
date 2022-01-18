package com.leathersheer.tools.SpiderUnit.Servlets.MengDian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

@WebServlet(name="CloumnCommitProcessServlet",urlPatterns = "/CloumnCommitProcess")
public class cloumnCommitProcess extends HttpServlet {
    public static final Logger CommitLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommitLogger.debug("enter cloumnCommitPorcess");
        String insertdata = req.getParameter("insertdata");
        CommitLogger.debug("insertdata=="+insertdata);
    }
}
