package com.leathersheer.tools.SpiderUnitTest;

import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SpiderTester {
    public static final Logger spiderTesterLogger= LogManager.getLogger();

    public static void main(String[] args){
        spiderTesterLogger.debug("Here we go!");

    }
}
