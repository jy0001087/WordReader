package com.leathersheer.tools.SpiderUnit;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;

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

            spiderLogger.trace("----------访问URL信息------- ：  " + url);

            spiderLogger.trace("----------head 信息如下----------");
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
            spiderLogger.trace("----------head 信息结束----------");

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

    public static void main(String[] args){
        String addresselements = "大兴-大兴其它-南海家园三里 / 66㎡ /南 北 / 1室1厅1卫 / 高楼层 （15层）";
        String[] addressArray = addresselements.split("/");

        for (String text:addressArray){
            //spiderLogger.trace("现在处理："+text);
            int house_element_order = 0;
            house_element_order = text.matches(".+-+.+")?1:house_element_order;
            house_element_order = text.matches(".+㎡+.+")?2:house_element_order;
            house_element_order = text.matches(".+室+.+")?3:house_element_order;
            house_element_order = text.matches("东?南?西?北?")?4:house_element_order;
            house_element_order = text.matches(".+层+.+")?5:house_element_order;
            switch(house_element_order){
                case 1:
                    spiderLogger.trace("地址："+text);
                    break;
                case 2:
                    spiderLogger.trace("面积："+text);   // 面积(面积格式为 XX平方米，转为数字面积)
                    break;
                case 3:
                spiderLogger.trace("户型："+text); //户型
                    break;
                case 4:
                spiderLogger.trace("朝向："+text);//朝向
                    break;
                case 5:
                spiderLogger.trace("楼层："+text);//楼层
                    break;
                default:
                    break;
            }
        }
    }
}
