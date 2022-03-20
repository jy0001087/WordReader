package com.leathersheer.tools.SpiderUnit.Servlets;

import com.leathersheer.tools.SpiderUnit.LianJiaUnits.LianJiaSpider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/Timmer")
public class Timmer extends HttpServlet {

    public static final Logger TimmerLogger = LogManager.getLogger();
    public static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(5);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    request.getRequestDispatcher("/ChengDuRealEstateStatement").forward(request, response);
                } catch (Exception e) {
                    TimmerLogger.error("[9999]定时任务启动异常！！");
                    TimmerLogger.error(e.toString(), e);
                }
            }
        }, 0, 1, TimeUnit.MINUTES);//0表示首次执行任务的延迟时间，40表示每次执行任务的间隔时间，TimeUnit.MILLISECONDS执行的时间间隔数值单位
    }

    /**
     * 测试用main，修改不会影响web应用内时间
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new LianJiaSpider(), 0, 12, TimeUnit.HOURS);
        System.out.println(Timmer.service);
    }
}
