package com.leathersheer.tools.SpiderUnit.Servlets.MengDian;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.Servlets.HouseMapper;
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
    public static final Logger LoginLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        LoginLogger.debug("The login mumber is : "+username);

        //业务查询逻辑
        ArrayList incentives = this.getIncentives();

        for(int i=0;i<incentives.size();i++) {
            LoginLogger.debug("This is result of Incentives :" + incentives.get(i));
        }
        request.getRequestDispatcher("/WEB-INF/jsp/incentives.jsp").forward(request,response);
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
