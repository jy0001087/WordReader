package com.leathersheer.tools.SpiderUnit.Servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

@WebServlet("/HouseMapper")
public class HouseMapper extends HttpServlet {
    public static final Logger HouseMapperLogger = LogManager.getLogger();

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/WEB-INF/jsp/housemapper.jsp").forward(request, response);
        } catch (Exception e) {
            HouseMapperLogger.error("跳转housemapper.jsp发生异常");
            HouseMapperLogger.error(e.toString(), e);
        }

    }

}