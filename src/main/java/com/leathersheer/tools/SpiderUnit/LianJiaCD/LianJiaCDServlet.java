package com.leathersheer.tools.SpiderUnit.LianJiaCD;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/LianJiaCDServlet",name = "LianJiaCDServlet")
public class LianJiaCDServlet extends HttpServlet {
    public static final Logger LJCDLogger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //
        String url = getInitiateUrl("urls.json","CDjinke","initiateurl");
        Spider spider = new Spider();
        spider.setHttpClient();
        Document doc = spider.getContent(url, Document.class);
        LianjiaCDHTMLBean htmlBean = this.getHouseInfoArray(doc);
        while(!(htmlBean.nextUrl.equals("NotExist"))){
            Pattern pattern = Pattern.compile("https://[a-z]*\\.[a-z]*\\.[a-z]*/");
            Matcher matcher = pattern.matcher(url);
            String nextUrl="";
            if(matcher.find()) {
                nextUrl = matcher.group(0)+htmlBean.nextUrl;
            }
            doc = spider.getContent(nextUrl,Document.class);
            LianjiaCDHTMLBean nextHtmlBean = this.getHouseInfoArray(doc);
            htmlBean.houseList.addAll(nextHtmlBean.houseList);
            htmlBean.nextUrl=nextHtmlBean.nextUrl;
        }
        this.saveToDB(htmlBean.houseList);
        PrintWriter out = null;
        out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String fileListArrayStringValue = mapper.writeValueAsString("get LianJiaCDServlet");
        out.print(fileListArrayStringValue);
    }

    public String getInitiateUrl(String filename,String propertySubjson,String propertyName){
        String url="";
        JSONObject json = this.getPropertiesJson(filename);
        JSONObject subJson = json.getJSONObject(propertySubjson);
        url = subJson.getString(propertyName);
        return url;
    }

    public JSONObject getPropertiesJson(String filename){
        JSONObject json=null;
        ServletConfig config = this.getServletConfig();
        String path = config.getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"classes"+File.separator+"properties"+File.separator+filename;
        File propertiesFile = new File(path);
        String jsonString="";
        try {
            if (propertiesFile.isFile()) {
                FileInputStream filein = new FileInputStream(propertiesFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(filein));
                String line="";
                while((line = reader.readLine())!=null){
                    jsonString=jsonString+line;
                }
            }
            json = new JSONObject(jsonString);
        }catch(Exception e){
            LJCDLogger.error("File read error",e);
        }
        return json;
    }

    public LianjiaCDHTMLBean getHouseInfoArray(Document doc){
        Elements elelemts = doc.select("li[class^=clear]");
        ArrayList<LianjiaCDBean> houseList = new ArrayList<>();
        for(Element element:elelemts){
            LianjiaCDBean bean = new LianjiaCDBean();
            bean.houseid=element.attr("data-lj_action_housedel_id"); //获取房屋id
            bean.title=element.select("div[class=title]").get(0).text(); //获取标题
            String houseinfoString = element.select("div[class=houseInfo]").get(0).text(); //获取房屋信息字串
            String[] houseinfoStringarray = houseinfoString.split("\\|");
            for(int i =0; i< houseinfoStringarray.length;i++){
                String segment = houseinfoStringarray[i].replaceAll(" ","");
                if(Pattern.matches(".室.厅",segment)){
                    bean.housetype=segment;
                }else if(Pattern.matches(".*平米",segment)){
                    bean.proportion=Float.valueOf(segment.replaceAll("平米",""));
                }else if(Pattern.matches("[东,西,南,北]",segment)){
                    bean.orientation=segment;
                }else if(Pattern.matches(".*装.*",segment)){
                    bean.decoration=segment;
                }else if(Pattern.matches(".*层.*",segment)){
                    bean.floor=segment;
                }
            }
            bean.followinfo=element.select("div[class=followInfo]").get(0).text();
            //bean.taxfree=element.select("div[class=tag] span[class=taxfree]").get(0).text();
            //TODO:税费信息有的条目没有，观察有必要再说。
            bean.price=Float.valueOf(element.select("div[class=totalPrice totalPrice2] span").get(0).text());
            bean.fetchdate=new Timestamp(new Date().getTime());
            bean.url=element.select("a[class=noresultRecommend img LOGCLICKDATA]").get(0).attr("href");
            houseList.add(bean);
        }
        LianjiaCDHTMLBean htmlBean = new LianjiaCDHTMLBean();
        htmlBean.houseList=houseList;
        try {
            Element nextPage = doc.select("div[class=page-box house-lst-page-box]").get(0);
            String pageUrl = nextPage.attr("page-url");
            String pageData= nextPage.attr("page-data");
            JSONObject pageDatajson = new JSONObject(pageData);
            int currentPageNumb = (Integer) pageDatajson.get("curPage");
            int totalPageNumb = (Integer) pageDatajson.get("totalPage");
            if(currentPageNumb < totalPageNumb){
                int nextPageNumb= currentPageNumb+1;
                String nextPageUrl=pageUrl.replace("{page}",String.valueOf(nextPageNumb));
                htmlBean.nextUrl=nextPageUrl;
            }else{
                htmlBean.nextUrl="NotExist";
            }
        }catch(Exception e){
            LJCDLogger.error("获取下一页数失败：",e);
            htmlBean.nextUrl="NotExist";
        }
        return htmlBean;
    }

    /**
     * 二手房数据存储
     * @param beanList
     */
    public void saveToDB(ArrayList<LianjiaCDBean> beanList){
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            LianjiaCDMapper mapper = sqlsession.getMapper(LianjiaCDMapper.class);
            try {
                db.dblogger.info("开始插入数据");
                for (LianjiaCDBean bean : beanList) {
                    mapper.insertColumn(bean);
                }
            } catch (Exception e) {
                db.dblogger.error("数据插入异常：");
                db.dblogger.error(e.toString(), e);
            }
            sqlsession.commit();
        }
    }
}
