package com.leathersheer.tools.SpiderUnit.AI;

import com.leathersheer.tools.SpiderUnit.Exceptions.ArgumentNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ChengDuRealEstateStatementProcesser {
    public static final Logger statementLogger = LogManager.getLogger();

    public ArrayList<ArrayList> tableToArray(Document doc){
        ArrayList<ArrayList> resultlist = new ArrayList<>();
        Elements  elements = doc.getElementsByClass("rightContent");//获取包含数据表格div
        if (elements.size() != 1) {
                statementLogger.error("rightContent not found! And elements size is  " + elements.size());
                throw new ArgumentNotFoundException("rightContent not found! And elements size is  " + elements.size());
            }
        elements = elements.get(0).getElementsByTag("tbody"); //获取第二个表格内容
        elements = elements.get(1).getElementsByTag("tr");  //获取tr节点
        for(int i=2;i<elements.size();i++){
            Elements innerElements = elements.get(i).getElementsByTag("td");
            ArrayList<String> innerList = new ArrayList<>();
            for(int j=0;j<innerElements.size();j++){
               innerList.add(innerElements.get(j).text());
            }
            resultlist.add(innerList);
        }
        return resultlist;
    }
}
