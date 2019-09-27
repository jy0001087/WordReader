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

public class Spider {
    public static final Logger spiderLogger = LogManager.getLogger();

    public Document getContent(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        Document doc = new Document("empty element");

        HttpGet httpGet=new HttpGet(url);
        try {
            httpGet.setHeader("User-Agent","spider");
            response = httpclient.execute(httpGet);

            spiderLogger.trace("----------访问URL信息------- ：  "+url);

            spiderLogger.trace("----------head 信息如下----------");
            Header[] heads = response.getAllHeaders();
            for(int i=0;i<heads.length;i++) {
                spiderLogger.trace(heads[i]);
            }
            spiderLogger.trace("----------head 信息结束----------");

            String html=EntityUtils.toString(response.getEntity());
            spiderLogger.info("页面编码为 ： "+ this.getEncoding(html)+"  "+response.getStatusLine());
            doc = Jsoup.parse(html);

            spiderLogger.trace("----------原始页面信息如下----------");
            spiderLogger.trace(new String(html.getBytes("UTF-8"),"UTF-8"));
            spiderLogger.trace("----------原始页面信息结束----------");

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
    };

    public String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
}
