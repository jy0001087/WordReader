package com.leathersheer.tools.SpiderUnit.AI;

import com.leathersheer.tools.SpiderUnit.AI.DBUnits.ChengDuHouseBean;
import com.leathersheer.tools.SpiderUnit.AI.DBUnits.ChengDuHouseMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.jsoup.nodes.Document;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name="ChengDuRealEstateStatement",urlPatterns = "/ChengDuRealEstateStatement")
public class ChengDuHouseStatement extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse rep){
        //TODO 暂时不知道写什么功能 空着
        return;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse rep){
        Spider spider = new Spider();
        String url = req.getAttribute("url").toString();
        Document doc=spider.getContent(url,Document.class);
        ChengDuRealEstateStatementProcesser processer = new ChengDuRealEstateStatementProcesser();
        //处理doc
        ArrayList<ArrayList> captureList = processer.tableToArray(doc);
        insertClounm(captureList);
    }

    public void insertClounm(ArrayList<ArrayList> list){
        ArrayList<ChengDuHouseBean> cloumnlist = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            ChengDuHouseBean columnBean = new ChengDuHouseBean();
            columnBean.area_name=list.get(i).get(0).toString();
            columnBean.total_area_proportion=list.get(i).get(1).toString();
            columnBean.residiential_house_num=list.get(i).get(2).toString();
            columnBean.residiential_house_proportion=list.get(i).get(3).toString();
            columnBean.not_residiential_house_proportion=list.get(i).get(4).toString();
            columnBean.city_name="ChengDu";
            columnBean.record_time=new Timestamp(new Date().getTime());
            cloumnlist.add(columnBean);
        }

            DBTools db = new DBTools();
            try (SqlSession sqlsession = db.getSqlSession().openSession()) {
                ChengDuHouseMapper mapper = sqlsession.getMapper(ChengDuHouseMapper.class);
                try {
                    db.dblogger.info("开始插入数据");
                    for (ChengDuHouseBean cloumnBean : cloumnlist) {
                        mapper.insertColumn(cloumnBean);
                    }
                } catch (Exception e) {
                    db.dblogger.error("数据插入异常：");
                    db.dblogger.error(e.toString(), e);
                }
                sqlsession.commit();
            }
        }
}
