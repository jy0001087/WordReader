package com.leathersheer.tools.SpiderUnit.PubToolUnit;

import com.leathersheer.tools.SpiderUnit.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

public class GaoDeUnit {
    public static final Logger GaoDeLogger = LogManager.getLogger();
    public static String GaoDeLocateUrl = "https://restapi.amap.com/v3/geocode/geo?";
    public static String GaoDeKey = "a962cf357d200d7c0735d90426a7b8b3"; //从高德处获取该参数

    public static void main(String[] args) {
        GaoDeLogger.trace("-----进入GaoDeUnit------");
        new GaoDeUnit().getLocate("北京", "朝阳-首都机场-首都机场南路西里");
    }

    /**
     * city 城市中文名 address 具体到门牌号，转换为经纬度信息
     */
    public String getLocate(String city, String address) {
        String requestUrl = GaoDeLocateUrl + "address=" + address + "&key=" + GaoDeKey + "&city=" + city;
        Spider spider = new Spider();
        String location = new String();
        JSONObject json = spider.getContent(requestUrl, JSONObject.class);
        try {
            for (int i = 0; i < Integer.parseInt((String) json.get("count")); i++) {
                JSONArray jsarray = json.getJSONArray("geocodes");
                JSONObject jsarrayobj = jsarray.getJSONObject(i);
                location = jsarrayobj.get("location").toString();
            }
        } catch (Exception e) {
            GaoDeLogger.error("[9999]获取高德返回异常" + json);
            return "地址数据获取异常";
        }
        GaoDeLogger.trace(json);
        return location;
    }
}