package com.leathersheer.tools.SpiderUnit.Servlets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.SpiderBeans.HouseBean;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

@WebServlet("/HouseMapper")
public class HouseMap extends HttpServlet {
    public static final Logger HouseMapperLogger = LogManager.getLogger();

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        List houselist = this.getHouse();

        try {
            request.setAttribute("houseinfolist", houselist);
            request.getRequestDispatcher("/WEB-INF/jsp/housemapper.jsp").forward(request, response);
        } catch (Exception e) {
            HouseMapperLogger.error("跳转housemapper.jsp发生异常");
            HouseMapperLogger.error(e.toString(), e);
        }

    }

    public List getHouse() {
        DBTools db = new DBTools();
        List houselist = new ArrayList();

        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            HouseMapper mapper = sqlsession.getMapper(HouseMapper.class);

            try {
                db.dblogger.debug("开始查询数据");
                houselist = mapper.getAllHouseInfo();
            } catch (Exception e) {
                db.dblogger.error("数据查询异常：");
                db.dblogger.error(e.toString(), e);
            }
        }
        return houselist;
    }

}