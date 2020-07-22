package com.leathersheer.tools.SpiderUnit.Servlets;

import com.leathersheer.tools.SpiderUnit.LianjiaUnits.LianJiaSpider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet("/Timmer")
public class Timmer extends HttpServlet {

    public static final Logger TimmerLogger = LogManager.getLogger();
    public static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            service.scheduleWithFixedDelay(new LianJiaSpider(),0,10, TimeUnit.HOURS);
            request.setAttribute("TimmerMessage","定时任务启动！");
            request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
        } catch (Exception e) {
            TimmerLogger.error("[9999]定时任务启动异常！！");
            TimmerLogger.error(e.toString(), e);
        }
    }

    /**
     * 测试用main，修改不会影响web应用内时间
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new LianJiaSpider(),0,12, TimeUnit.HOURS);
        System.out.println(Timmer.service);
    }
}
