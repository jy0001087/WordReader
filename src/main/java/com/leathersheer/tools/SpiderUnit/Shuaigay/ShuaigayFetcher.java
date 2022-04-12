package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class ShuaigayFetcher {
    public static final Logger shuaigayLogger = LogManager.getLogger();

    public void doFetch(String url){
        Spider spider = new Spider();
        Map<String,String> para= new HashMap<String,String>();
        para.put("username","1123");
        para.put("password","1234");
        //spider.getCookieswithPara(url,para);
        spider.getContentwithCookies(url,Document.class);
    }

    public static void main(String[] args){
        ShuaigayFetcher sf=new ShuaigayFetcher();
        //sf.doFetch("https://www.shuaigay6.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LhLvE&inajax=1");
        sf.doFetch("https://www.shuaigay6.com/forum.php?mod=viewthread&tid=1536090&highlight=%C2%CC%C5%AB%D3%FB%CD%FB");
    }

}
