package com.leathersheer.tools.SpiderUnit.Listeners;

import com.leathersheer.tools.SpiderUnit.Shuaigay.ShuaigayFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ShuaigayListener implements ServletContextListener {

    public static final Logger SGlogger = LogManager.getLogger();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        ServletContext context=sce.getServletContext();

        SGlogger.info("ShuaiGay Listener is started");

        Calendar cal = Calendar.getInstance();
        // 每天定点执行
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);

        Timer timer=new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                SGlogger.info("ShuaiG-15-Timer Start");
                new ShuaigayFetcher().doSocialGameGrab(context);
            }
        },cal.getTime(),24 * 60 * 60 * 1000);

        Calendar ca2 = Calendar.getInstance();
        // 每天定点执行
        ca2.set(Calendar.HOUR_OF_DAY, 22);
        ca2.set(Calendar.MINUTE, 00);
        ca2.set(Calendar.SECOND, 00);

        timer.schedule(new TimerTask(){
            public void run(){
                SGlogger.info("ShuaiG-22-Timer Start");
                new ShuaigayFetcher().doSocialGameGrab(context);
            }
        },ca2.getTime(),24 * 60 * 60 * 1000);

        timer.schedule(new TimerTask(){
            public void run(){
                SGlogger.info("ShuaiG is alive!");
            }
        },60*1000,60*60*1000);
    }
}
