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
        String path="海淀-甘家口-西三环北路86号院 / 49.00㎡ /西北 / 2室1厅1卫 / 地下室 （7层）";
        String[] paths=path.split("/");
        ShellHouseBean mbean = new ShellHouseBean();
        for (int i = 0; i < paths.length; i++) {
            String segment = paths[i].replaceAll(" ", "");
            if (Pattern.matches(".室.厅.卫", segment)) {
                mbean.housetype = segment;
            } else if (Pattern.matches(".*㎡", segment)) {
                mbean.proportion = Float.valueOf(segment.replaceAll("㎡", ""));
            }  else if (Pattern.matches(".+-.+-.+", segment)) {
                mbean.address = segment;
            }
        }
        System.out.println("测试");
    }
}
