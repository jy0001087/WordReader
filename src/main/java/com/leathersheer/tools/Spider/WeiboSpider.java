package com.leathersheer.tools.Spider;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WeiboSpider extends Spider {
    public static final Logger workerLogger = LogManager.getLogger();
    public static void main(String[] args){
        System.out.println("WeiboSpider test  ok!");
        workerLogger.trace("WeiboSpider logger start!");
        Spider spider = new WeiboSpider();
        Document doc = spider.getContent();
        workerLogger.info(doc.getElementsByClass("movie-list list").toString());
    }

    @Override
    public Document getContent() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        Document doc = new Document("empty element");

        HttpGet httpGet=new HttpGet("https://weibo.com");
        try {
            httpGet.setHeader("User-Agent","spider");
            response = httpclient.execute(httpGet);
            workerLogger.trace("----------head 信息如下----------");
            Header[] heads = response.getAllHeaders();
            for(int i=0;i<heads.length;i++) {
                workerLogger.trace(heads[i]);
            }
            workerLogger.trace("----------head 信息结束----------");
            String html=EntityUtils.toString(response.getEntity());
            workerLogger.info("页面编码为 ： "+ super.getEncoding(html)+"  "+response.getStatusLine());
            doc = Jsoup.parse(html);
            workerLogger.trace("----------原始页面信息如下----------");
            workerLogger.trace(new String(html.getBytes("UTF-8"),"UTF-8"));
            workerLogger.trace("----------原始页面信息结束----------");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return doc;
    }
}
