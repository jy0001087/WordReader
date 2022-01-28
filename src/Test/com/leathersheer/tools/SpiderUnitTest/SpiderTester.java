package com.leathersheer.tools.SpiderUnitTest;

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
        request.setAttribute("url","https://flights.ctrip.com/online/list/oneway-xnn-bjs?depdate=2022-02-04&cabin=y_s_c_f&adult=1&child=0&infant=0&containstax=1");

        AmenityServlet amenity=new AmenityServlet();
        try {
            amenity.doPost(request, response);
        }catch(Exception e){
            spiderTesterLogger.error(e);
        }
    }
}
