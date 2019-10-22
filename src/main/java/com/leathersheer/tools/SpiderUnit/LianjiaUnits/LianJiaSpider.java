package com.leathersheer.tools.SpiderUnit.LianjiaUnits;

import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.leathersheer.tools.SpiderUnit.*;

import com.leathersheer.tools.SpiderUnit.PubToolUnit.GaoDeUnit;
import com.leathersheer.tools.SpiderUnit.SpiderBeans.HouseBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LianJiaSpider extends Spider {
    public static final Logger LianjiaLogger = LogManager.getLogger();

    public static void main(String[] args) {
        LianjiaLogger.trace("-----------LianjiaSpider start!!");
        LianJiaSpider spider = new LianJiaSpider();
        spider.getHouse(spider.getContent("https://bj.lianjia.com/zufang/rt200600000001/", Document.class));// 北京房源搜索-整租
    }

    /**
     * 提取房源地址信息
     */
    public LinkedList<HouseBean> getHouse(Document doc) {
        Elements elements = doc.getElementsByClass("content__list--item");// 筛选页面中包含的房源信息块
        Elements addresselements = new Elements();
        HouseBean house = new HouseBean();
        GaoDeUnit gaode = new GaoDeUnit();
        LinkedList<HouseBean> houselist = new LinkedList<>();
        if (elements.isEmpty()) {
            LianjiaLogger.error("未找到目标class内容:content__list--item");
        } else {
            for (Element element : elements) {
                addresselements = element.getElementsByClass("content__list--item--des");// 筛选房源信息中的具体信息字符串
                if (addresselements.size() == 1) {
                    LianjiaLogger.trace("整体房屋情况字串：" + addresselements.text());
                    String[] addressArray = addresselements.text().split("/");
                    house.address = addressArray[0].replace(" ","");    // 地址
                    house.area = this.getDigit(addressArray[1]);    // 面积(面积格式为 XX平方米，转为数字面积)
                    house.oriented = addressArray[2].replace(" ","");
                    house.housetype = addressArray[3].replace(" ","");
                    house.floor = addressArray[4].replace(" ","");
                } else {
                    LianjiaLogger.error("单条content__list--item中存在两个地址信息，查看网页源代码搂错误");
                    return houselist;
                }
                // 获取价格部分（价格格式为 “XXXX 元/月”）
                house.price = this.getDigit(element.getElementsByClass("content__list--item-price").text());
                //获取高德location信息
                house.GDlocation = gaode.getLocate("北京", house.address);
                houselist.add(house);
                LianjiaLogger.info("房源地址：" + house.address + "  面积 ：" + house.area + " 价格：" + house.price+" GDlocation :"+house.GDlocation);
            }
        }
        LianjiaLogger.info("本页共找到房源----" + houselist.size() + "个");
        return houselist;
    }

    public Integer getDigit(String text) {
        Integer result = null;
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(text);
        if (m.find()) {
            result = Integer.parseInt(m.group());
        } else {
            LianjiaLogger.error("正则表达式匹配错误---匹配原字串为：" + text);
            return null;
        }
        return result;
    }
}