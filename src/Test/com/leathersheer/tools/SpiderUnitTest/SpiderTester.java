package com.leathersheer.tools.SpiderUnitTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderTester {
    public static final Logger spiderTesterLogger= LogManager.getLogger();

    public static void main(String[] args) throws JsonProcessingException {

        String str = "找粗口语音视频";
        System.out.println(str.contains("粗口"));
    }
}
