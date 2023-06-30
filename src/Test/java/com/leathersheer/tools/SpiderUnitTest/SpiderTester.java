package com.leathersheer.tools.SpiderUnitTest;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderTester {
    public static final Logger spiderTesterLogger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        String url = "https://www.shuaitong7.xyz/forum.php?mod=viewthread&tid=1632775&highlight=%C2%CC&mobile=2";
        Pattern p = Pattern.compile("tid=\\d+");
        Matcher m = p.matcher(url);
        if(m.find()) {
            Pattern innerP = Pattern.compile("\\d+");
            Matcher innerM = innerP.matcher(m.group());
            if (innerM.find()) {
                spiderTesterLogger.info(innerM.group());
            }
        }
    }
}
