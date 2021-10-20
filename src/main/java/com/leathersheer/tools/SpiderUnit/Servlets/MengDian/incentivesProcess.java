package com.leathersheer.tools.SpiderUnit.Servlets.MengDian;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "incentiveProcessServlet", urlPatterns="/IncentivesProcess")
public class incentivesProcess extends HttpServlet {
    public static final Logger MengdianLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String querydata = request.getParameter("querydata");
        String ajaxflag = request.getParameter("ajaxflag");
        ArrayList incentives = new ArrayList();
        if (querydata != null) {
            MengdianLogger.debug("querydata is not null it's :" + querydata);
            incentives= this.getIncentivesWithData(querydata);
        } else {
            //业务查询逻辑
            incentives = this.getIncentives();
        }
        ObjectMapper mapper = new ObjectMapper();
        String incentivesforArray = mapper.writeValueAsString(incentives);
        if(ajaxflag == null) {
            request.setAttribute("incentivesforArray", incentivesforArray);
            MengdianLogger.debug("enter forward! to /WEB-INF/jsp/incentives.jsp and ajaxflag = "+ajaxflag);
            request.getRequestDispatcher("/WEB-INF/jsp/incentives.jsp").forward(request, response);
        }else if(ajaxflag.equals("ajax")){
            PrintWriter out = null;
            try{
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.print(incentivesforArray);
            }catch(Exception e){
                MengdianLogger.error("get outwriter error: "+e.getStackTrace());
            }
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

    public ArrayList getIncentivesWithData(String data){
        DBTools db = new DBTools();
        ArrayList incentives = new ArrayList();

        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            IncentivesMapper mapper = sqlsession.getMapper(IncentivesMapper.class);

            try {
                db.dblogger.info("开始查询数据");
                incentives = mapper.getIncentivesWithData(data);
            } catch (Exception e) {
                db.dblogger.error("数据查询异常：");
                db.dblogger.error(e.toString(), e);
            }
        }
        return incentives;
    }
}
