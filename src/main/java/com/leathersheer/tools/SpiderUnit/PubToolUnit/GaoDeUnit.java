package com.leathersheer.tools.SpiderUnit.PubToolUnit;

import com.leathersheer.tools.SpiderUnit.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class GaoDeUnit {
    public static final Logger GaoDeLogger = LogManager.getLogger();
    public static String GaoDeLocateUrl = "https://restapi.amap.com/v3/geocode/geo?";
    public static String GaoDeKey = "a962cf357d200d7c0735d90426a7b8b3";

    public static void main(String[] args) {
        GaoDeLogger.trace("-----进入GaoDeUnit------");
        new GaoDeUnit().getLocate("北京", "丰台-看丹桥-新华街七里");
    }

    /**
     * city 城市中文名 address 具体到门牌号
     */
    public void getLocate(String city, String address) {
        String requestUrl = GaoDeLocateUrl + "address=" + address + "&key=" + GaoDeKey + "&city=" + city;
        Spider spider = new Spider();
        JSONObject doc = spider.getContent(requestUrl, JSONObject.class);
        try {
            for (int i = 0; i < Integer.parseInt((String)doc.get("count")); i++) {
                
            }
        } catch (Exception e) {
            GaoDeLogger.error("非数字输入");
        }
        GaoDeLogger.trace(doc);
    }
}