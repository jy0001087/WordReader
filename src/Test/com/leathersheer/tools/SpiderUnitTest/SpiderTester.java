package com.leathersheer.tools.SpiderUnitTest;

import com.leathersheer.tools.SpiderUnit.AI.ChengDuHouseStatement;
import com.leathersheer.tools.SpiderUnit.AmenityUnits.AmenityServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SpiderTester {
    public static final Logger spiderTesterLogger= LogManager.getLogger();

    public static void main(String[] args){
        spiderTesterLogger.debug("Here we go!");
        MockHttpServletRequest request=new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setAttribute("url","https://www.cdzjryb.com/");

        ChengDuHouseStatement target=new ChengDuHouseStatement();
        try {
            target.doPost(request, response);
        }catch(Exception e){
            spiderTesterLogger.error(e);
        }
    }
}
