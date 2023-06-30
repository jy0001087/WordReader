package com.leathersheer.tools.SpiderUnitTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import com.leathersheer.tools.SpiderUnit.ShellRental.ShellHouseBean;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SMArticleBean;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SMArticlePostBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderTester {
    public static final Logger spiderTesterLogger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        String url = "https://www.shuaitong7.xyz/forum.php?mod=viewthread&tid=1632775&highlight=%C2%CC&mobile=2"
        Pattern p = Pattern.compile("tid=\\d+");
        Matcher m = p.matcher(url);
        while(m.find()){
            System.out.println(m.group());
        }
    }
}
