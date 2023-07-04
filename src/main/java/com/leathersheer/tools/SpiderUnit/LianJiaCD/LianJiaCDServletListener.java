package com.leathersheer.tools.SpiderUnit.LianJiaCD;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesReader;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/LianJiaCDServlet", name = "LianJiaCDServlet")
public class LianJiaCDServletListener extends HttpServlet implements ServletContextListener {
    public static final Logger LJCDLogger = LogManager.getLogger();
    public ServletContext context=null;

    //手动更新
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String fileListArrayStringValue = mapper.writeValueAsString(this.Engine());
        out.print(fileListArrayStringValue);
    }

    //定时任务
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
        context = sce.getServletContext();
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    LJCDLogger.info("LianjiaCD Listener is alive!");
                    try {
                        PropertiesReader.Builder builder = new PropertiesReader.Builder(context, "properties.json");
                        PropertiesReader pr = builder.setPropertyPath("CDjinke|executeTime").build();
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        Date targetDate = df.parse(pr.getProperty("executeTime"));
                        Date now = df.parse(df.format(new Date()));
                        if (now.after(targetDate)) {
                            LJCDLogger.info("LianjiaCD Listener is executing!");
                            Engine();
                        }
                    } catch (ParseException e) {
                        LJCDLogger.error("转换时间出现异常：\n", e);
                    } catch (Throwable e){
                        LJCDLogger.error("线程执行异常 \n",e);
                    }
                }
            }, 0, 1, TimeUnit.HOURS);
    }

    public String Engine(){
        if(context==null){//servlet调用时，获取ServletContext
            context = this.getServletContext();}
        PropertiesReader.Builder builder= new PropertiesReader.Builder(context,"properties.json");
        PropertiesReader pr = builder.setPropertyPath("CDjinke|initiateurl").build();
        String url = pr.getProperty("initiateurl");

        Spider spider = new Spider();
        Document doc = spider.getContent(url, Document.class);
        LianjiaCDHTMLBean htmlBean = this.getHouseInfoArray(doc);
        while (!(htmlBean.nextUrl.equals("NotExist"))) {
            Pattern pattern = Pattern.compile("https://[a-z]*\\.[a-z]*\\.[a-z]*/");//匹配网址最开始部份
            Matcher matcher = pattern.matcher(url);
            String nextUrl = "";
            if (matcher.find()) {
                nextUrl = matcher.group(0) + htmlBean.nextUrl;
            }
            doc = spider.getContent(nextUrl, Document.class);
            LianjiaCDHTMLBean nextHtmlBean = this.getHouseInfoArray(doc);
            htmlBean.houseList.addAll(nextHtmlBean.houseList);
            htmlBean.nextUrl = nextHtmlBean.nextUrl;
        }
        this.saveToDB(htmlBean.houseList);
        return "OK";
    }

    public LianjiaCDHTMLBean getHouseInfoArray(Document doc) {
        Elements elelemts = doc.select("li[class^=clear]");
        ArrayList<LianjiaCDBean> houseList = new ArrayList<>();
        for (Element element : elelemts) {
            LianjiaCDBean bean = new LianjiaCDBean();
            bean.houseid = element.attr("data-lj_action_housedel_id"); //获取房屋id
            bean.title = element.select("div[class=title]").get(0).text(); //获取标题
            String houseinfoString = element.select("div[class=houseInfo]").get(0).text(); //获取房屋信息字串
            String[] houseinfoStringarray = houseinfoString.split("\\|");
            for (int i = 0; i < houseinfoStringarray.length; i++) {
                String segment = houseinfoStringarray[i].replaceAll(" ", "");
                if (Pattern.matches(".室.厅", segment)) {
                    bean.housetype = segment;
                } else if (Pattern.matches(".*平米", segment)) {
                    bean.proportion = Float.valueOf(segment.replaceAll("平米", ""));
                } else if (Pattern.matches("[东,西,南,北]", segment)) {
                    bean.orientation = segment;
                } else if (Pattern.matches(".*装.*", segment)) {
                    bean.decoration = segment;
                } else if (Pattern.matches(".*层.*", segment)) {
                    bean.floor = segment;
                }
            }
            bean.followinfo = element.select("div[class=followInfo]").get(0).text();
            //bean.taxfree=element.select("div[class=tag] span[class=taxfree]").get(0).text();
            //TODO:税费信息有的条目没有，观察有必要再说。
            bean.price = Float.valueOf(element.select("div[class=totalPrice totalPrice2] span").get(0).text());
            bean.fetchdate = new Timestamp(new Date().getTime());
            bean.updatedate = bean.fetchdate; // 解决使用updatedate排序有空值的问题。
            bean.url = element.select("a[class=noresultRecommend img LOGCLICKDATA]").get(0).attr("href");
            houseList.add(bean);
        }
        LianjiaCDHTMLBean htmlBean = new LianjiaCDHTMLBean();
        htmlBean.houseList = houseList;
        try {
            Element nextPage = doc.select("div[class=page-box house-lst-page-box]").get(0);
            String pageUrl = nextPage.attr("page-url");
            String pageData = nextPage.attr("page-data");
            JSONObject pageDatajson = new JSONObject(pageData);
            int currentPageNumb = (Integer) pageDatajson.get("curPage");
            int totalPageNumb = (Integer) pageDatajson.get("totalPage");
            if (currentPageNumb < totalPageNumb) {
                int nextPageNumb = currentPageNumb + 1;
                String nextPageUrl = pageUrl.replace("{page}", String.valueOf(nextPageNumb));
                htmlBean.nextUrl = nextPageUrl;
            } else {
                htmlBean.nextUrl = "NotExist";
            }
        } catch (Exception e) {
            LJCDLogger.error("获取下一页数失败：", e);
            htmlBean.nextUrl = "NotExist";
        }
        return htmlBean;
    }

    /**
     * 二手房数据存储
     *
     * @param beanList
     */
    public void saveToDB(ArrayList<LianjiaCDBean> beanList) {
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            LianjiaCDMapper mapper = sqlsession.getMapper(LianjiaCDMapper.class);
            int i = 0;
            for (LianjiaCDBean bean : beanList) {
                i=i+1;
                try {
                    db.dblogger.info("开始插入数据，目前id:" +bean.houseid+"第 "+i +" 条，共 "+ beanList.size()+" 条数据");
                    mapper.insertColumn(bean);
                    sqlsession.commit();
                } catch (Exception e) {
                    if (e.getMessage().contains("duplicate key")) {//更新已存在记录状态
                        db.dblogger.info("Record already exist,to update updatedate filed");
                        bean.updateFlag="needToUpdate";
                        sqlsession.rollback();
                    }else {
                        db.dblogger.error("数据插入异常：", e);
                    }
                }
                if(bean.updateFlag.equals("needToUpdate")) {
                    LianjiaCDBean formerBean = new LianjiaCDBean();
                    try {//价格未变化的，只更新updatedate，价格变了更新price和followinfo
                        formerBean = mapper.selectHouseState(bean);
                        if(formerBean.price!=bean.price){
                            db.dblogger.info("Price updated ,The record will backup into histTable ,houseid = " + bean.houseid);
                            mapper.updateWithPriceAndFollowinfo(bean);
                            bean.updateFlag="needToBackUp";
                        }else {
                            db.dblogger.info("Updating updatedate filed,houseid = " + bean.houseid);
                             mapper.updateOnlyWithDate(bean);
                        }
                        sqlsession.commit();
                    } catch (Exception e) {
                        db.dblogger.error("update error", e);
                        sqlsession.rollback();
                    }
                    //老数据写入lianjiacdhousehist表，hist表无主键
                    if(bean.updateFlag.equals("needToBackUp")) {
                        try {
                            db.dblogger.info("Backup to histTable,houseid = " + bean.houseid);
                            mapper.insertColumnHist(formerBean);
                            sqlsession.commit();
                        } catch (Exception e) {
                            db.dblogger.error("备份老数据出现异常",e);
                            sqlsession.rollback();
                        }
                    }
                }
            }
            sqlsession.close();
        }
        db.dblogger.info("本次插入结束，共完成数据更新 " + beanList.size() +" 条。");
    }
}
