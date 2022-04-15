package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class ShuaigayFetcher {
    public static final Logger shuaigayLogger = LogManager.getLogger();

    public void doFetch(String url,Spider spider,String step){
        spider.setOriginUrl(url);

        switch(step) {
            case "login":
                Map<String, String> para = new HashMap<String, String>();
                para.put("username", "jy0001087");
                para.put("password", "woai28552@");
                spider.setHttpClient();
                spider.setPara(para);
                spider.getCookieswithPara();
            break;
            case "fetch":
                spider.getContentwithCookies();
        }
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            shuaigayLogger.error("线程休眠异常",e);
        }
    }

    public static void main(String[] args){
        ShuaigayFetcher sf=new ShuaigayFetcher();
        Spider spider= new Spider();
        String url = "https://www.shuaigay6.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LhLvE&inajax=1";
        sf.doFetch(url,spider,"login");
        url = "https://www.shuaigay6.com/forum.php?mod=viewthread&tid=1536090";
        sf.doFetch(url,spider,"fetch");
    }

}
