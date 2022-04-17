package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseBean;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseMapper;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.ShuaigayMapper;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SociaGameBean;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.*;

public class ShuaigayFetcher {
    public static final Logger shuaigayLogger = LogManager.getLogger();

    public Document doFetch(String url,Spider spider,String step){
        Document doc = null;
        spider.setOriginUrl(url);

        switch(step) {
            case "login":
                Map<String, String> para = new HashMap<String, String>();
                para.put("username", "jy0001087");
                para.put("password", "woai28552@");
                spider.setHttpClient();
                spider.setPara(para);
                spider.getCookieswithPara();
            break;
            case "fetch":
                doc = spider.getContentwithCookies(Document.class);
        }
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            shuaigayLogger.error("线程休眠异常",e);
        }
        return  doc;
    }

    public ArrayList<SociaGameBean> fileTitleList(Map<String,ArrayList<String>> keywordMap, JSONObject target , Document doc ){
        ArrayList<SociaGameBean> beanList = new ArrayList<>();
        ArrayList<Map<String,String>> paraList = this.getParainfoFromJson(target);
        Map<String,String> uidMap = paraList.get(0);
        if(!(uidMap.get("beanParaName").equals("uid"))){
            shuaigayLogger.error("json内容异常，未找到uid内容");
            return null;
        }
        Elements uidElements=doc.select(uidMap.get("beanParaLocation"));
        for(Element element : uidElements){
            int addFlag = 0 ;
            SociaGameBean bean = new SociaGameBean();
            for(Map<String,String> jsonMap:paraList) {
                Elements finalElements = element.select(jsonMap.get("beanParaLocation"));
                Element finalElement = finalElements.get(0);
                String currentParaName = jsonMap.get("beanParaName");
                switch (currentParaName) {
                    case "uid":
                        bean.uid = finalElement.attr(jsonMap.get("beanParaValue"));
                        break;
                    case "url":
                        bean.url = finalElement.attr(jsonMap.get("beanParaValue"));
                        break;
                    case "postTitle":
                        bean.postTitle = finalElement.text();
                        ArrayList<String> keywordList = keywordMap.get("postTitle");
                        for(String str : keywordList){
                            if(bean.postTitle.contains(str)){
                                addFlag =1;
                            }
                        }
                        break;
                }
            }
            if(addFlag == 1) {
                beanList.add(bean);
            }
        }
        shuaigayLogger.info("finish");
        return beanList;
    }

    public ArrayList<Map<String,String>> getParainfoFromJson(JSONObject subJson){
        ArrayList paraList = new ArrayList();
        Iterator it = subJson.keys();
        while(it.hasNext()){
            Map<String ,String > jsoutResoutMap = new HashMap<>();
            String key  = (String)it.next();
            jsoutResoutMap.put("beanParaName",key);
            Object interValue = subJson.get(key);
            if(interValue instanceof String){
                String value = interValue.toString();
            }else if(interValue instanceof JSONObject){
                JSONObject value = subJson.getJSONObject(key);
                Iterator it1 = value.keys();
                while(it1.hasNext()){
                    String location = (String)it1.next();
                    String paraValue = (String)value.get(location);
                    jsoutResoutMap.put("beanParaLocation",location);
                    jsoutResoutMap.put("beanParaValue",paraValue);
                }
            }
            paraList.add(jsoutResoutMap);
        }
        return paraList;
    }

    public void saveToDB(ArrayList<SociaGameBean> beanList){
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            ShuaigayMapper mapper = sqlsession.getMapper(ShuaigayMapper.class);
            try {
                db.dblogger.info("开始插入数据");
                for (SociaGameBean bean : beanList) {
                    mapper.insertColumn(bean);
                }
            } catch (Exception e) {
                db.dblogger.error("数据插入异常：");
                db.dblogger.error(e.toString(), e);
            }
            sqlsession.commit();
        }
    }

    public void doGrab(){
        ShuaigayFetcher sf=new ShuaigayFetcher();
        Spider spider= new Spider();
        String url = "https://www.shuaigay6.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LhLvE&inajax=1";
        sf.doFetch(url,spider,"login");
        //url = "https://www.shuaigay6.com/forum.php?mod=viewthread&tid=1536090";
        url = "https://www.shuaigay6.com/forum-125-1.html";  // SocialGame-url
        Document doc=sf.doFetch(url,spider,"fetch");
        Map<String,ArrayList<String>> keywordMap = new HashMap<>();
        ArrayList<String> kewordList = new ArrayList<String>() {{
            add("北京");add("1m");add("锁");add("粗口");add("sub");add("dom");add("羞辱");add("丝袜");add("控制");add("乳胶");    }};
        keywordMap.put("postTitle",kewordList);
        JSONObject tarJsobj = new JSONObject("{\"uid\":{\"tbody[id~=^normalthread]\":\"id\"},\"url\":{\"tbody[id~=^normalthread] a[class=s xst]\":\"href\"},\"postTitle\":{\"tbody[id~=^normalthread] a[class=s xst]\":\"text\"}}"
        );
        ArrayList<SociaGameBean> beanList = sf.fileTitleList(keywordMap,tarJsobj,doc);
        sf.saveToDB(beanList);
    }

    public static void main(String[] args){
        ShuaigayFetcher sf=new ShuaigayFetcher();
        Spider spider= new Spider();
        String url = "https://www.shuaigay6.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LhLvE&inajax=1";
        sf.doFetch(url,spider,"login");
        //url = "https://www.shuaigay6.com/forum.php?mod=viewthread&tid=1536090";
        url = "https://www.shuaigay6.com/forum-125-1.html";  // SocialGame-url
        Document doc=sf.doFetch(url,spider,"fetch");
        Map<String,ArrayList<String>> keywordMap = new HashMap<>();
        ArrayList<String> kewordList = new ArrayList<String>() {{
            add("北京");add("1m");add("锁");add("粗口");add("sub");add("dom");add("羞辱");add("丝袜");add("控制");add("乳胶");    }};
        keywordMap.put("postTitle",kewordList);
        JSONObject tarJsobj = new JSONObject("{\"uid\":{\"tbody[id~=^normalthread]\":\"id\"},\"url\":{\"tbody[id~=^normalthread] a[class=s xst]\":\"href\"},\"postTitle\":{\"tbody[id~=^normalthread] a[class=s xst]\":\"text\"}}"
        );
        ArrayList<SociaGameBean> beanList = sf.fileTitleList(keywordMap,tarJsobj,doc);
        sf.saveToDB(beanList);
    }

}
