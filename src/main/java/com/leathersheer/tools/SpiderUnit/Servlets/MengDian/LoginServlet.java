package com.leathersheer.tools.SpiderUnit.Servlets.MengDian;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns="/Login")
public class LoginServlet extends HttpServlet {
    public static final Logger LoginLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        LoginLogger.debug("The login mumber is : "+username);
        request.getRequestDispatcher("/WEB-INF/jsp/incentives.jsp").forward(request,response);
    }
}
