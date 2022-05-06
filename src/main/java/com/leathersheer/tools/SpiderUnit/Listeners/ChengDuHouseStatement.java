package com.leathersheer.tools.SpiderUnit.Listeners;

import com.leathersheer.tools.SpiderUnit.Exceptions.ArgumentNotFoundException;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseBean;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ChengDuHouseStatement  {
    public static final Logger CDLogger = LogManager.getLogger();

    public void doGrab(String tarurl){
        Spider spider = new Spider();
        String url = tarurl;
        spider.setHttpClient();
        Document doc=spider.getContent(url,Document.class);
        //处理doc
        ArrayList<ArrayList> captureList = this.tableToArray(doc);
        insertClounm(captureList);
    }

    public ArrayList<ArrayList> tableToArray(Document doc){
        ArrayList<ArrayList> resultlist = new ArrayList<>();
        Elements elements = doc.getElementsByClass("rightContent");//获取包含数据表格div
        if (elements.size() != 1) {
            CDLogger.error("rightContent not found! And elements size is  " + elements.size());
            throw new ArgumentNotFoundException("rightContent not found! And elements size is  " + elements.size());
        }
        elements = elements.get(0).getElementsByTag("tbody"); //获取第二个表格内容
        elements = elements.get(1).getElementsByTag("tr");  //获取tr节点
        for(int i=2;i<elements.size();i++){
            Elements innerElements = elements.get(i).getElementsByTag("td");
            ArrayList<String> innerList = new ArrayList<>();
            for(int j=0;j<innerElements.size();j++){
                innerList.add(innerElements.get(j).text());
            }
            resultlist.add(innerList);
        }
        return resultlist;
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

        public static void main(String[] args){
            new ChengDuHouseStatement().doGrab("https://www.cdzjryb.com/");
        }
}
