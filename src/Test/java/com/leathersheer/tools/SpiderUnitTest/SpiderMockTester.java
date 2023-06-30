package com.leathersheer.tools.SpiderUnitTest;

import org.springframework.mock.web.MockHttpServletRequest;

public class SpiderMockTester {
    public static void main(String[] args){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.example.com");
        request.setRequestURI("/shuaigayServlet");
        request.setQueryString("threadId=12345");
    }

}
