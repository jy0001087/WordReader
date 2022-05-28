package com.leathersheer.tools.SpiderUnit.LianJiaCD;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
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
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/LianJiaCDServlet",name = "LianJiaCDServlet")
public class LianJiaCDServlet extends HttpServlet {
    public static final Logger LJCDLogger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = getInitiateUrl("urls.json","CDjinke","initiateurl");
        Spider spider = new Spider();
        spider.setHttpClient();
        Document doc = spider.getContent(url, Document.class);
        ArrayList<LianjiaCDBean> houselist = this.getHouseInfoArray(doc);

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

    public ArrayList<LianjiaCDBean> getHouseInfoArray(Document doc){
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
                    bean.proportion=segment.replaceAll("平米","");
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
            bean.price=element.select("div[class=totalPrice totalPrice2] span").get(0).text();
            bean.updatedate=new Timestamp(new Date().getTime());
            bean.url=element.select("a[class=noresultRecommend img LOGCLICKDATA]").get(0).attr("href");
            houseList.add(bean);
        }
        return houseList;
    }
}
