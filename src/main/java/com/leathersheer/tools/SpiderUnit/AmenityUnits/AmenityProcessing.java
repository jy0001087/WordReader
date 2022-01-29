package com.leathersheer.tools.SpiderUnit.AmenityUnits;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
//处理页面信息
public class AmenityProcessing {
    public static final Logger amenityProcessingLogger = LogManager.getLogger();

    public static ArrayList<Element> elementCapture(Document doc,ArrayList<String> targetlist){
        ArrayList<Element> captureResult = new ArrayList<Element>();
        for(String targetname : targetlist) {
            Element element = doc.getElementById(targetname);
            if (element == null) {
                //找不到id为targetname的元素，则new Element element=<CanNotFind id="CanNotFind">targetname</CanNotFind>,避免找不到后面直接抛异常
                Document emptydoc= Jsoup.parse("<CanNotFind id=\"CanNotFind\"></CanNotFind>");
                element = emptydoc.getElementById("CanNotFind").text(targetname);
            }
                captureResult.add(element);
        }
        for(Element element:captureResult){
            amenityProcessingLogger.debug(element.data());
        }
        return captureResult;
    }
}
