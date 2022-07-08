package com.leathersheer.tools.SpiderUnit.ShellRental;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.LianJiaCD.LianjiaCDBean;
import com.leathersheer.tools.SpiderUnit.LianJiaCD.LianjiaCDMapper;
import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesObj;
import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesReader;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name="ShellRental",urlPatterns = "/ShellRental")
public class ShellRentalServletListener extends HttpServlet implements ServletContextListener {
    public static final Logger SRSLogger = LogManager.getLogger();
    public ServletContext context=null;
    private ArrayList<ShellHouseBean> mHouseList = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        Engine();
        out.println("done!");
    }

    public void Engine(){
        if(context==null){//servlet调用时，获取ServletContext
            context = this.getServletContext();}
        PropertiesReader.Builder builder= new PropertiesReader.Builder(context,"properties.json");
        PropertiesReader pr = builder.setPropertyPath("ShellRental|initiateurl").build();
        PropertiesObj urlObj = pr.getPropertiesObj();

        HashMap<String,String> urlMap = urlObj.propertiesMap;
        for(Map.Entry<String,String> entry:urlMap.entrySet()){
            String url = entry.getValue();
            String landmark = entry.getKey();
            Spider spider = new Spider();
            Document doc = spider.getContent(url, Document.class);
            Pattern pattern = Pattern.compile("https://[a-z]*\\.[a-z]*\\.[a-z]*\\.[a-z]*/");//匹配网址最开始部份
            Matcher matcher = pattern.matcher(url);
            String baseUrl = "";
            if (matcher.find()) {
                baseUrl = matcher.group(0);
            }

            //翻页引擎
            String nextUrl = getRentalHouseList(doc,landmark);;
            while(!(nextUrl.equals("NotExist"))) {
                nextUrl = baseUrl+nextUrl;
                doc = spider.getContent(nextUrl, Document.class);
                nextUrl = getRentalHouseList(doc,landmark);
            }
        }

        saveToDB(mHouseList);
        SRSLogger.debug("pause");
    }

    public String getRentalHouseList(Document doc,String landmark){
        Elements elements = doc.select("div[class=content__list]").get(0).select("div[class=content__list--item]");
        String nextUrl = "NotExist";
        for(Element element:elements){
            try {
                ShellHouseBean mbean = new ShellHouseBean();
                mbean.houseid = element.attr("data-house_code");
                mbean.url = element.select("a[class=content__list--item--aside]").get(0).attr("href");
                mbean.address = element.select("p[class=content__list--item--des]").get(0).text();
                mbean.price = Integer.valueOf(element.select("em").get(0).text());
                mbean.landmark = landmark;
                String[] paths=mbean.address.split("/");
                for (int i = 0; i < paths.length; i++) {
                    String segment = paths[i].replaceAll(" ", "");
                    if (Pattern.matches(".室.厅.卫", segment)) {
                        mbean.housetype = segment;
                    } else if (Pattern.matches(".*㎡", segment)) {
                        mbean.proportion = Float.valueOf(segment.replaceAll("㎡", ""));
                    }  else if (Pattern.matches(".+-.+-.+", segment)) {
                        mbean.address = segment;
                    } else if (Pattern.matches(".+层.?", segment)){
                        mbean.floor=segment;
                    }
                }
                mHouseList.add(mbean);
            }catch(Exception e){
                SRSLogger.error("房屋异常： href = " + element+"\n", e);
            }
        }
        //获取下一页
        try {
            Element mPages = doc.select("div[class=content__pg]").get(0);
            Integer totalPage = Integer.valueOf(mPages.attr("data-totalpage"));
            Integer curPage = Integer.valueOf(mPages.attr("data-curpage"));
            String pageUrl = mPages.attr("data-url");
            if (curPage < totalPage) {
                int nextPageNumb = curPage + 1;
                String nextPageUrl = pageUrl.replace("{page}", String.valueOf(nextPageNumb));
                nextUrl = nextPageUrl;
            } else {
                nextUrl = "NotExist";
            }
        } catch (Exception e) {
            SRSLogger.error("获取下一页数失败：", e);
            nextUrl = "NotExist";
        }
        return nextUrl;
    }

    /**
     * 租房数据存储
     *
     * @param beanList
     */
    public void saveToDB(ArrayList<ShellHouseBean> beanList) {
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            ShellRentalMapper mapper = sqlsession.getMapper(ShellRentalMapper.class);
            int i = 0;
            for (ShellHouseBean bean : beanList) {
                i=i+1;
                try {
                    db.dblogger.info("开始插入数据，目前id:" +bean.houseid+"第 "+i +" 条，共 "+ beanList.size()+" 条数据");
                    mapper.insertAll(bean);
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
                /*
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
                 */
            }
            sqlsession.close();
        }
        db.dblogger.info("本次插入结束，共完成数据更新 " + beanList.size() +" 条。");
    }
}
