package com.leathersheer.tools.SpiderUnit.Servlets;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.SpiderBeans.HouseBean;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

@WebServlet("/HouseMapper")
public class HouseMap extends HttpServlet {
    public static final Logger HouseMapperLogger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        HouseMap hm = new HouseMap();
        HouseMapperLogger.trace("HouseMap Servlet start！！");
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<HouseBean> list = new ArrayList<>();
        HouseBean house = new HouseBean();
        house.address="111";
        house.house_code="BF123123";
        house.area=300;
        list.add(house);
        HouseBean house1 = new HouseBean();
        house1.address="111";
        house1.house_code="BF123123";
        house1.area=300;
        list.add(house1);
        String json = mapper.writeValueAsString(list);
        HouseMapperLogger.trace(json);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        ArrayList houselist = this.getHouse();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String HouseInfoJson = mapper.writeValueAsString(houselist);
            request.setAttribute("HouseInfoJson", HouseInfoJson);
            request.getRequestDispatcher("/WEB-INF/jsp/housemapper.jsp").forward(request, response);
        } catch (Exception e) {
            HouseMapperLogger.error("跳转housemapper.jsp发生异常");
            HouseMapperLogger.error(e.toString(), e);
        }

    }

    public ArrayList getHouse() {
        DBTools db = new DBTools();
        ArrayList houselist = new ArrayList();

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