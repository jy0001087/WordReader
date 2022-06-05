package com.leathersheer.tools.AndroidResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/DataServlet" ,name = "DataServlet")
public class DataServlet extends HttpServlet {
    public static final Logger DataLogger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");

        DataLogger.debug("DataServlet is started!");
        try{
            PrintWriter out = resp.getWriter();
            JSONObject outJson = new JSONObject("{\"status\":\"ok\"}");
            out.write(outJson.toString());
        }catch(Exception e){
            DataLogger.error("写入返回数据异常",e);
        }
    }
}
