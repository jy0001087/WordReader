package com.leathersheer.tools.AndroidResponse.DataFragment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.LianJiaCD.LianjiaCDBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/DataServlet", name = "DataServlet")
public class DataServlet extends HttpServlet {
    public static final Logger DataLogger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String para = req.getParameter("para");
        if(para == null){
            DataLogger.debug("para is null :\n");
            para = "nohist";
        }
        JSONObject outJson ;
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");

        DataLogger.debug("DataServlet is started!");
        try {
            outJson = getQyeryData(para);
            PrintWriter out = resp.getWriter();
            out.write(outJson.toString());
        } catch (Exception e) {
            DataLogger.error("写入返回数据异常", e);
        }
    }

    private JSONObject getQyeryData(String para) {
        JSONObject json = new JSONObject();
        DBTools db = new DBTools();
        List<LianjiaCDBean> list ;
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            DataServletMapper mapper = sqlsession.getMapper(DataServletMapper.class);
            if (para.equals("hist")) {
                list = mapper.getHouseHist();
            } else {
                list = mapper.getHouseInfoAll();
            }
            ObjectMapper jsonHelper = new ObjectMapper();
            Map<String, String> jsonMap = new HashMap<>();
            for (LianjiaCDBean bean : list) {
                jsonMap.put(bean.houseid, jsonHelper.writeValueAsString(bean));
            }
            json = new JSONObject(jsonMap);
            DataLogger.info("DataServlet/para="+para+", 查询结束，获取 " + list.size() + " 条数据");
        } catch (Exception e) {
            DataLogger.error("DataServlet/para="+para+", QyeryError \n", e);
        }
        return json;
    }
}
