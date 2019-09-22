package com.leathersheer.tools.Spider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LianJiaSpider extends Spider {
    public static final Logger LianjiaLogger = LogManager.getLogger();
    
    public static void main(String[] args){
        LianjiaLogger.trace("-----------LianjiaSpider start!!");
        new LianJiaSpider().getContent("https://bj.lianjia.com/zufang/rt200600000001/");
    }
}