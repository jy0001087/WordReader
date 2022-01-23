package com.leathersheer.tools.SpiderUnit.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="AmenityServlet",urlPatterns = "/Amenity")
public class AmenityServlet extends HttpServlet {
    //get方法仅用于实现jsp页面的访问
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/amenity.jsp").forward(req,resp);
    }
    // 业务逻辑写在post方法里，再返回给amenity页面
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO  在这里实现爬虫爬取数据的功能

        req.getRequestDispatcher("/WEB_INF/jsp/amenity.jsp").forward(req,resp);
    }
}
