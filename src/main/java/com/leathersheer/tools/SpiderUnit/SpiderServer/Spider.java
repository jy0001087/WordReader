package com.leathersheer.tools.SpiderUnit.SpiderServer;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
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
    public Map<String,String> para = new HashMap<>();
    public String originUrl = "";
    public CloseableHttpClient httpClient=null;
    public HttpClientContext context = null;

    public void setPara(Map<String,String> para){
        this.para = para;
    }
    public void setOriginUrl(String url){
        this.originUrl=url;
    }
    public void setHttpClient(){
        // 全局请求设置
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        // 创建cookie store的本地实例
        CookieStore cookieStore = new BasicCookieStore();
        // 创建HttpClient上下文
        this.context = HttpClientContext.create();
        this.context.setCookieStore(cookieStore);
        // 创建一个HttpClient
        this.httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(cookieStore).build();
    }

    /**
     * HttpGet模块
     * 使用泛型，在调用时确定需要返回数据类型，
     * url为要访问的目标地址，T为要返回的数据类型
     */
    public <T> T getContent(String url, Class<T> type) {
        CloseableHttpResponse response = null;
        T doc = null;

        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setHeader("User-Agent", "spider");
            response = httpClient.execute(httpGet);
            doc=this.getDoctype(response);
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
     * 获取cookies,并放入httpclient公用。
     * setter 方法设置url 和 para
     * @return context 包含cookies信息
     */
    public <T> T getCookieswithPara() {
        T doc = null;
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(originUrl);
        Map<String,String> cookies = new HashMap<>();
        //设置访问参数
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0");
        // 传入para处理
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        for(Map.Entry<String, String> entry : para.entrySet()){
            parameters.add(new BasicNameValuePair(entry.getKey(),entry.getValue())); }
        try {
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
            response = httpClient.execute(httpPost);
            doc = this.getDoctype(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doc;
    }

    /**
     * 设置cookies后，返回登录后网站内容doc。支持泛型
     * @param <T>
     * @return
     */
    public <T> T getContentwithCookies(Class<T> type) {
        T doc = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(originUrl);
        try {
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0");
            response = httpClient.execute(httpGet,context);
            doc = getDoctype(response);
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


    //工具类
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

    public <T> T getDoctype(CloseableHttpResponse response) throws Exception {
        T doc = null;
        spiderLogger.trace("----------访问URL信息------- ：  \n" + originUrl);
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
            } else if (heads[i].toString().contains("html")){
                responseType= "xml";
                spiderLogger.info("得到xml类型返回");
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
        return doc;
    }

}
