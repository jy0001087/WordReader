package com.leathersheer.tools.SpiderUnitTest;

import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderTester {
    public static final Logger spiderTesterLogger= LogManager.getLogger();

    public static void main(String[] args){
        spiderTesterLogger.debug("Here we go!");
        String s= "https://cd.lianjia.com/ershoufang/106110540150.html";
        String[] res=s.split("/");
        spiderTesterLogger.debug( res);

        new ChengDuHouseStatement().doGrab("https://www.cdzjryb.com/");
    }
}
