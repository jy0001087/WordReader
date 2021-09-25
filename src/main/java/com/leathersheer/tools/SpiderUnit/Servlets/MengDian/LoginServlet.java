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
            String querydata = request.getParameter("querydata");
            ArrayList incentives = new ArrayList();
            MengdianLogger.debug("The login mumber is : " + username);
            if (querydata != null) {
                MengdianLogger.debug("querydata is not null it's :" + querydata);

            } else {
                //业务查询逻辑
                incentives = this.getIncentives();
            }
            ObjectMapper mapper = new ObjectMapper();
            String incentivesforArray = mapper.writeValueAsString(incentives);
            request.setAttribute("incentivesforArray", incentivesforArray);
            request.getRequestDispatcher("/WEB-INF/jsp/incentives.jsp").forward(request, response);
        }
    }

    public ArrayList getIncentives() {
        DBTools db = new DBTools();
        ArrayList incentives = new ArrayList();

        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            IncentivesMapper mapper = sqlsession.getMapper(IncentivesMapper.class);

            try {
                db.dblogger.info("开始查询数据");
                incentives = mapper.getIncentives();
            } catch (Exception e) {
                db.dblogger.error("数据查询异常：");
                db.dblogger.error(e.toString(), e);
            }
        }
        return incentives;
    }
}
