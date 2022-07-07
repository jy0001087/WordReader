package com.leathersheer.tools.SpiderUnit.ShellRental;

import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesObj;
import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesReader;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
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
        SRSLogger.debug("pause");
    }

    public String getRentalHouseList(Document doc,String landmark){
        Elements elements = doc.select("div[class=content__list]").get(0).select("div[class=content__list--item]");
        String nextUrl = "NotExist";
        for(Element element:elements){
            ShellHouseBean mbean = new ShellHouseBean();
            mbean.houseid= element.attr("data-house_code");
            mbean.url = element.select("a[class=twoline]").get(0).attr("href");
            mbean.address = element.select("p[class=content__list--item--des]").get(0).text();
            mbean.price = Integer.valueOf(element.select("em").get(0).text());
            mbean.landmark=landmark;
            mHouseList.add(mbean);
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
}
