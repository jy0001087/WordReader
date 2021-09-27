package com.leathersheer.tools.SpiderUnit.Servlets.MengDian;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

@WebServlet(name = "LoginServlet", urlPatterns="/Login")
public class LoginServlet extends HttpServlet {
    public static final Logger MengdianLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");  //MDKJ
        String passwd = request.getParameter("password");   //Nmwc666888
        String veryfiledcode= request.getParameter("validatecode");

        MengdianLogger.debug("username = "+username+"|| password = "+passwd+"|| vcode = "+veryfiledcode);
        if(!username.equals("MDKJ")||!passwd.equals("Nmwc666888")){
            request.getSession().setAttribute("Alert","用户名或密码无效，请重新输入！");
            request.getRequestDispatcher("/WEB-INF/jsp/loginmengdian.jsp").forward(request,response);
        }else if(!veryfiledcode.equals(request.getSession().getAttribute("vCode").toString())){
            request.getSession().setAttribute("Alert","验证码错误，请重新输入");
            request.getRequestDispatcher("/WEB-INF/jsp/loginmengdian.jsp").forward(request,response);
        }else {
            request.getRequestDispatcher("/IncentivesProcess").forward(request,response);
        }
    }
}
