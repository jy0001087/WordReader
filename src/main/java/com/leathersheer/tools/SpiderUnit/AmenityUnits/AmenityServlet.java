package com.leathersheer.tools.SpiderUnit.AmenityUnits;

import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name="AmenityServlet",urlPatterns = "/Amenity")
public class AmenityServlet extends HttpServlet {
    public static final Logger amenityLogger = LogManager.getLogger();

    //get方法仅用于实现jsp页面的访问
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/amenity.jsp").forward(req,resp);
    }
    // 业务逻辑写在post方法里，再返回给amenity页面
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Spider amenitySpider = new Spider();
        String url = req.getAttribute("url").toString();
        Document doc=amenitySpider.getContent(url,Document.class);
        ArrayList captureString = new ArrayList();
        captureString.add("__NEXT_DATA__");
        ArrayList<Element> captureList= AmenityProcessing.elementCapture(doc,captureString);
        //TODO 先实现功能，后期优化json解析逻辑
        JSONObject json = new JSONObject(captureList.get(0).data());
        JSONObject json1 = (JSONObject) json.get("query");
        JSONObject json2 = (JSONObject)json1.get("GlobalSearchCriteria");
        String id = json2.getString("transactionID");
        
        req.getRequestDispatcher("/WEB_INF/jsp/amenity.jsp").forward(req,resp);
    }

}
