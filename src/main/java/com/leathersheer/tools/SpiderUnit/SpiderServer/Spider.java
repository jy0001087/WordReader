package com.leathersheer.tools.SpiderUnit.SpiderServer;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spider {
    public static final Logger spiderLogger = LogManager.getLogger();
    public String responseType = "";

    /**
     * HttpGet模块
     * 使用泛型，在调用时确定需要返回数据类型，
     * url为要访问的目标地址，T为要返回的数据类型
     */
    public <T> T getContent(String url, Class<T> type) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        T doc = null;

        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setHeader("User-Agent", "spider");
            response = httpclient.execute(httpGet);

            spiderLogger.trace("----------访问URL信息------- ：  \n" + url);

            spiderLogger.trace("----------response head 信息如下----------");
            Header[] heads = response.getAllHeaders();
            for (int i = 0; i < heads.length; i++) {
                spiderLogger.trace(heads[i]);
                if (heads[i].toString().contains("json")) {
                    responseType = "json";
                    spiderLogger.info("得到json类型返回");
                } else if (heads[i].toString().contains("html")) {
                    responseType = "html";
                    spiderLogger.info("得到html类型返回");
                }
            }
            spiderLogger.trace("----------response head 信息结束----------");

            String html = EntityUtils.toString(response.getEntity());
            spiderLogger.info("页面编码为 ： " + this.getEncoding(html) + "  " + response.getStatusLine());
            spiderLogger.trace("----------原始页面信息如下----------");
            spiderLogger.trace(new String(html.getBytes("UTF-8"), "UTF-8"));
            spiderLogger.trace("----------原始页面信息结束----------");

            switch (responseType) {
            case "html":
                doc = (T) Jsoup.parse(html);
                break;
            case "json":
                JSONObject jsonObject = new JSONObject(html);
                doc = (T) jsonObject;
                break;
            default:
                spiderLogger.error("未返回正确格式的response");
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) doc;
    };


    /**
     * 获取cookies
     * @param url
     * @param para
     */
    public void getCookieswithPara(String url, Map<String,String> para) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        Map<String,String> cookies = new HashMap<>();
        //设置访问参数
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0");

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        parameters.add(new BasicNameValuePair("username", "jy0001087"));
        parameters.add(new BasicNameValuePair("password", "woai28552@"));

        try {
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
            response = httpclient.execute(httpPost);

            spiderLogger.trace("----------访问URL信息------- ：  \n" + url);

            spiderLogger.trace("----------response head 信息如下----------");
            Header[] heads = response.getAllHeaders();
            for (int i = 0; i < heads.length; i++) {
                spiderLogger.trace(heads[i]);
                if (heads[i].toString().contains("json")) {
                    responseType = "json";
                    spiderLogger.info("得到json类型返回");
                } else if (heads[i].toString().contains("html")) {
                    responseType = "html";
                    spiderLogger.info("得到html类型返回");
                }
            }
            Header[] cookiesHeader = response.getHeaders("Set-Cookie");
            for(int i=0;i<cookiesHeader.length;i++){
                //TODO 写cookei分解语句
            }

            spiderLogger.trace("----------response head 信息结束----------");

            String html = EntityUtils.toString(response.getEntity());
            spiderLogger.info("页面编码为 ： " + this.getEncoding(html) + "  " + response.getStatusLine());
            spiderLogger.trace("----------原始页面信息如下----------");
            spiderLogger.trace(new String(html.getBytes("UTF-8"), "UTF-8"));
            spiderLogger.trace("----------原始页面信息结束----------");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public <T> T getContentwithCookies(String url, Class<T> type) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        T doc = null;
        BasicCookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_saltkey","EGEuDPUA"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_lastvisit","1649654727"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_sid","OsXnWX"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_lastact","1649670501%09home.php%09spacecp"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_ulastactivity","8299Ho3g679aCepAQtBOwUuQaRnrj8WIpz7MQSx0BzHIYxpeovSZ"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_auth","4eeasJUPghAVqaH%2FUnNuI8fzBhesomfmiyaq7719STUUpfdjXxwx44bM53c%2B%2BPPrzr11jSfNISAKlxFXIdpwu39uh4Er"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_lastcheckfeed","1124819%7C1649670502"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_lip","114.254.2.244%2C1649670224"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_checkfollow","1"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_checkpm","1"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_viewid","tid_1536090"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_st_p","1124819%7C1649670501%7C6568d531d060ebd33f122469e318899b"));
        cookieStore.addCookie(new BasicClientCookie("jBy3_2132_seccodecSNT4E4t","168.ebae58b820aea91702"));


        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setHeader("User-Agent", "spider");
            httpGet.addHeader("Cookie",cookieStore.toString());

            response = httpclient.execute(httpGet);

            spiderLogger.trace("----------访问URL信息------- ：  \n" + url);

            spiderLogger.trace("----------response head 信息如下----------");
            Header[] heads = response.getAllHeaders();
            for (int i = 0; i < heads.length; i++) {
                spiderLogger.trace(heads[i]);
                if (heads[i].toString().contains("json")) {
                    responseType = "json";
                    spiderLogger.info("得到json类型返回");
                } else if (heads[i].toString().contains("html")) {
                    responseType = "html";
                    spiderLogger.info("得到html类型返回");
                }
            }
            spiderLogger.trace("----------response head 信息结束----------");

            String html = EntityUtils.toString(response.getEntity());
            spiderLogger.info("页面编码为 ： " + this.getEncoding(html) + "  " + response.getStatusLine());
            spiderLogger.trace("----------原始页面信息如下----------");
            spiderLogger.trace(new String(html.getBytes("UTF-8"), "UTF-8"));
            spiderLogger.trace("----------原始页面信息结束----------");

            switch (responseType) {
                case "html":
                    doc = (T) Jsoup.parse(html);
                    break;
                case "json":
                    JSONObject jsonObject = new JSONObject(html);
                    doc = (T) jsonObject;
                    break;
                default:
                    spiderLogger.error("未返回正确格式的response");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) doc;
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
