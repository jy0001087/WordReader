package com.leathersheer.tools.AndroidResponse.DataFragment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.LianJiaCD.LianjiaCDBean;
import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseBean;
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
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(urlPatterns = "/DataServlet", name = "DataServlet")
public class DataServlet extends HttpServlet {
    public static final Logger DataLogger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String para = req.getParameter("para");
        if (para == null) {
            DataLogger.debug("para is null :\n");
            para = "nohist";
        }
        JSONObject outJson = new JSONObject();
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");

        DataLogger.debug("DataServlet is started!");
        try {
            switch (para) {
                case "nohist":  //查询LianjiaCD表
                    outJson = getQyeryData(para);
                    break;
                case "realEstateInfo":   //查询realestatestatement表
                    outJson = getEstateInfo(para);
                    break;
            }
            PrintWriter out = resp.getWriter();
            out.write(outJson.toString());
        } catch (Exception e) {
            DataLogger.error("写入返回数据异常", e);
        }
    }

    /**
     * 查询LianjiaCD表数据
     *
     * @param para
     * @return
     */
    private JSONObject getQyeryData(String para) {
        JSONObject json = new JSONObject();
        DBTools db = new DBTools();
        List<LianjiaCDBean> list;
        List<LianjiaCDBean> listhist = new ArrayList<>();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            DataServletMapper mapper = sqlsession.getMapper(DataServletMapper.class);
            if (para.equals("hist")) {
                list = mapper.getHouseHist();
            } else {
                listhist = mapper.getHouseHist();
                list = mapper.getHouseInfoAll();
                list = mergeHist(listhist, list);
            }
            sqlsession.close();
            ObjectMapper jsonHelper = new ObjectMapper();
            Map<String, String> jsonMap = new HashMap<>();
            for (LianjiaCDBean bean : list) {
                jsonMap.put(bean.houseid, jsonHelper.writeValueAsString(bean));
            }
            json = new JSONObject(jsonMap);
            DataLogger.info("DataServlet/para=" + para + ", 查询结束，获取 " + list.size() + " 天的数据");
        } catch (Exception e) {
            DataLogger.error("DataServlet/para=" + para + ", QyeryError \n", e);
        }
        return json;
    }

    private List<LianjiaCDBean> mergeHist(List<LianjiaCDBean> hist, List<LianjiaCDBean> list) {
        //hist表数据清理，选择update时间最近的,每个houseid保留一条
        HashMap<String, LianjiaCDBean> histMap = new HashMap<>();
        for (int i = 0; i < hist.size(); i++) {
            LianjiaCDBean tempBean = hist.get(i);
            if (tempBean.updatedate == null) {
                tempBean.updatedate = tempBean.fetchdate; //修整部份hist表数据updatedate数据为空的问题
            }
            if (histMap.containsKey(tempBean.houseid)) {
                if (tempBean.updatedate.after(histMap.get(tempBean.houseid).updatedate)) {
                    histMap.put(tempBean.houseid, tempBean); //如果当前数据更新日期晚于histMap中存在的记录，则替换同HistMap中ID数据为当前数据
                }
            } else {
                histMap.put(tempBean.houseid, tempBean);
            }
        }

        //将hist数据融入list表数据，更该status为降价、涨价等。
        for (Map.Entry<String, LianjiaCDBean> entry : histMap.entrySet()) {
            for (int j = 0; j < list.size(); j++) {
                if (entry.getKey().equals(list.get(j).houseid)) {
                    list.get(j).originalFetchdate = entry.getValue().originalFetchdate;
                    list.get(j).originalPrice = entry.getValue().originalPrice;
                }
                if (list.get(j).originalFetchdate == null) {
                    list.get(j).status = "Equability";
                } else if (list.get(j).price > list.get(j).originalPrice) {
                    list.get(j).status = "Inflation";
                } else {
                    list.get(j).status = "Depreciate";
                }
            }
        }
        return list;
    }

    private JSONObject getEstateInfo(String para) {
        JSONObject mJson = new JSONObject();
        DBTools db = new DBTools();
        List<ChengDuHouseBean> mList;

        try (SqlSession sqlSession = db.getSqlSession().openSession()) {
            DataServletMapper mapper = sqlSession.getMapper(DataServletMapper.class);
            mList = mapper.getRealEstateInfo();
            sqlSession.close();
            HashMap<String, String> jsonMap = beanProcessor(mList);
            mJson = new JSONObject(jsonMap);
            DataLogger.info("DataServlet/para=" + para + ", 查询结束，获取 " + jsonMap.entrySet().size() + " 条数据");
        } catch (Exception e) {
            DataLogger.error("查询 realEstateState表异常：\n", e);
        }
        return mJson;
    }

    /**
     * 数据库表查询结果清洗
     *
     * @param mList
     * @return
     */
    private HashMap<String, String> beanProcessor(List<ChengDuHouseBean> mList) {
        List<ChengDuHouseBean> rList = new ArrayList<>();
        HashMap<Date, ChengDuHouseBean> tempMap = new HashMap<>();
        HashMap<String, String> rMap = new HashMap<>();
        ChengDuHouseBean tempBean;
        for (int i = 0; i < mList.size(); i++) {
            ChengDuHouseBean rBean;
            tempBean = mList.get(i);
            rBean = tempMap.get(tempBean.record_date);
            rBean = assigValue(tempBean, rBean);
            tempMap.put(rBean.record_date, rBean);
        }
        for (Map.Entry<Date, ChengDuHouseBean> entry : tempMap.entrySet()) {
            ObjectMapper jsonHelper = new ObjectMapper();
            try {
                rMap.put(String.valueOf(entry.getKey().getTime()), jsonHelper.writeValueAsString(entry.getValue()));
            } catch (Exception e) {
                DataLogger.error("Bean->Json error +bean.record_date = " + entry.getValue().record_time, e);
            }
        }
        return rMap;
    }

    private ChengDuHouseBean assigValue(ChengDuHouseBean sourceBean, ChengDuHouseBean targetBean) {
        switch (sourceBean.area_name) {
            case "郊区新城":
                if (targetBean == null || (Float.valueOf(targetBean.outSkirts_total_area_proportion) < Float.valueOf(sourceBean.total_area_proportion))) {
                    if(targetBean == null){
                        targetBean = new ChengDuHouseBean();
                    }
                    targetBean.outSkirts_not_residiential_house_proportion = sourceBean.not_residiential_house_proportion;
                    targetBean.outSkirts_residiential_house_num = sourceBean.residiential_house_num;
                    targetBean.outSkirts_residiential_house_proportion = sourceBean.residiential_house_proportion;
                    targetBean.outSkirts_total_area_proportion = sourceBean.total_area_proportion;
                    targetBean.record_time = sourceBean.record_time;
                    targetBean.record_date = sourceBean.record_date;
                    targetBean.city_name = sourceBean.city_name;
                }
                break;
            case "中心城区":
                if (targetBean == null || (Float.valueOf(targetBean.downTown_total_area_proportion) < Float.valueOf(sourceBean.total_area_proportion))) {
                    if(targetBean == null){
                        targetBean = new ChengDuHouseBean();
                    }
                    targetBean.downTown_not_residiential_house_proportion = sourceBean.not_residiential_house_proportion;
                    targetBean.downTown_residiential_house_num = sourceBean.residiential_house_num;
                    targetBean.downTown_residiential_house_proportion = sourceBean.residiential_house_proportion;
                    targetBean.downTown_total_area_proportion = sourceBean.total_area_proportion;
                    targetBean.record_time = sourceBean.record_time;
                    targetBean.record_date = sourceBean.record_date;
                    targetBean.city_name = sourceBean.city_name;
                }
                break;
            case "全市":
                if (targetBean == null || (Float.valueOf(targetBean.wholeUrban_total_area_proportion) < Float.valueOf(sourceBean.total_area_proportion))) {
                    if(targetBean == null){
                        targetBean = new ChengDuHouseBean();
                    }
                    targetBean.wholeUrban_not_residiential_house_proportion = sourceBean.not_residiential_house_proportion;
                    targetBean.wholeUrban_residiential_house_num = sourceBean.residiential_house_num;
                    targetBean.wholeUrban_residiential_house_proportion = sourceBean.residiential_house_proportion;
                    targetBean.wholeUrban_total_area_proportion = sourceBean.total_area_proportion;
                    targetBean.record_time = sourceBean.record_time;
                    targetBean.record_date = sourceBean.record_date;
                    targetBean.city_name = sourceBean.city_name;
                }
                break;
        }
        return targetBean;
    }
}


