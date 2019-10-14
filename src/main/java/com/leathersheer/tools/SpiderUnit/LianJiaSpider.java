package com.leathersheer.tools.SpiderUnit;

import java.util.LinkedList;

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
        LinkedList<HouseBean> houselist = new LinkedList<>();
        if (elements.isEmpty()) {
            LianjiaLogger.error("未找到目标class内容");
        } else {
            for (Element element : elements) {
                addresselements = element.getElementsByClass("content__list--item--des");// 筛选房源信息中的具体信息字符串
                if (addresselements.size() == 1) {
                    LianjiaLogger.trace("整体房屋情况："+addresselements.text());
                    String[] addressArray = addresselements.text().split("/");
                    house.address = addressArray[0];
                } else {
                    LianjiaLogger.error("单条content__list--item中存在两个地址信息，查看网页源代码搂错误");
                    return houselist;
                }
                houselist.add(house);
                LianjiaLogger.info("本页地址信息：" + house.address);
            }
        }
        return houselist;
    }
}