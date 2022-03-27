package com.leathersheer.tools.SpiderUnit.Listeners;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseBean;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseMapper;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ResoldHouseBean;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ResoldHouseMapper;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LianjiaResoldHouseStatement {
    public static final Logger resoldHouseLogger = LogManager.getLogger();

    public void doGrap(String url){
        Spider spider = new Spider();
        Document doc=spider.getContent(url,Document.class);
        ArrayList<ResoldHouseBean> captureList = this.htmlToArray(doc);
        insertClounm(captureList);
    }

    public ArrayList<ResoldHouseBean> htmlToArray(Document doc){
        ArrayList<ResoldHouseBean> result = new ArrayList<>();
        Elements elements = doc.getElementsByClass("info clear");
        for(int i=0;i< elements.size();i++){
            ResoldHouseBean resold = new ResoldHouseBean();
            Element tree=elements.get(i).getElementsByClass("title").get(0).getElementsByTag("a").get(0);
            resold.houseid=tree.attr("data-housecode");
            resold.houseUrl=tree.attr("href");
            String[] houseInfoArray = elements.get(i).getElementsByClass("houseInfo").get(0).text().split("\\|");
            resold.houseType= houseInfoArray[0];
            Pattern p = Pattern.compile("\\d+\\.*\\d+");
            Matcher m = p.matcher(houseInfoArray[1]);
            if(m.find()){resold.proportion=Double.valueOf(m.group());
            }else {resoldHouseLogger.error("resoldHouse proportion not found!");}
            resold.orientation=houseInfoArray[2];
            resold.mansion=houseInfoArray[4];
            resold.age = houseInfoArray[5];
            p = Pattern.compile("[^0-9]");
            resold.totalPrice=Double.valueOf(String.valueOf(p.matcher(
                    elements.get(i).getElementsByClass("totalPrice totalPrice2").get(0).text()).replaceAll("").trim()));
            resold.unitPrice=Double.valueOf(String.valueOf(p.matcher(
                    elements.get(i).getElementsByClass("unitPrice").get(0).text()).replaceAll("").trim()));
            // 遗下为固定获取部份
            resold.grabTime=new Timestamp(new Date().getTime());
            String[] source=resold.houseUrl.split("/");
            resold.source=source[2];
            resold.isSold="false";
            result.add(resold);
        }
        return result;
    }

    public void insertClounm(ArrayList<ResoldHouseBean> list){
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            ResoldHouseMapper mapper = sqlsession.getMapper(ResoldHouseMapper.class);
            try {
                db.dblogger.info("开始插入数据");
                for (ResoldHouseBean houseBean : list) {
                    mapper.insertColumn(houseBean);
                }
            } catch (Exception e) {
                db.dblogger.error("数据插入异常：");
                db.dblogger.error(e.toString(), e);
            }
            sqlsession.commit();
        }
    }

    public static void main(String[] args){
        new LianjiaResoldHouseStatement().doGrap("https://cd.lianjia.com/ershoufang/rs%E9%87%91%E7%A7%91%E5%A4%A9%E7%B1%81%E5%9F%8E/");
    }
}
