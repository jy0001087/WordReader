package com.leathersheer.tools.Spider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LianJiaSpider extends Spider {
    public static final Logger LianjiaLogger = LogManager.getLogger();
    
    public static void main(String[] args){
        LianjiaLogger.trace("-----------LianjiaSpider start!!");
        LianJiaSpider spider= new LianJiaSpider();
        spider.docFilter(spider.getContent("https://bj.lianjia.com/zufang/rt200600000001/"));//北京房源搜索-整租
    }
   
/**
 * 提取房源地址信息
 */
    public void docFilter(Document doc){
            Elements elements= doc.getElementsByClass("content__list--item");//筛选页面中包含的房源信息块
            if(elements.isEmpty()){
                LianjiaLogger.error("未找到目标class内容");
            }else{
                for(Element element:elements){
                    element.getElementById("content__list--item--des");//筛选房源信息中的具体信息字符串
                    LianjiaLogger.info("本页地址信息："+element.text() );
                }
            }
    }
}