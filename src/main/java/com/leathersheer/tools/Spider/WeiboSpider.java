package com.leathersheer.tools.Spider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

public class WeiboSpider extends Spider {
    public static final Logger workerLogger = LogManager.getLogger();
    
    public static void main(String[] args){
        System.out.println("WeiboSpider test  ok!");
        workerLogger.trace("WeiboSpider logger start!");
        Spider spider = new WeiboSpider();
        Document doc = spider.getContent("https://weibo.com");
        workerLogger.info(doc.getElementsByClass("movie-list list").toString());
    }
}
