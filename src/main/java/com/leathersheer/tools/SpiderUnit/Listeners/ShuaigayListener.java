package com.leathersheer.tools.SpiderUnit.Listeners;

import com.leathersheer.tools.SpiderUnit.Shuaigay.ShuaigayFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        SGlogger.info("ShuaiGay Listener is started");

        Calendar cal = Calendar.getInstance();
        // 每天定点执行
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);

        Timer timer=new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                SGlogger.info("Timer Start");
                new ShuaigayFetcher().doSocialGameGrab();
            }
        },cal.getTime(),24 * 60 * 60 * 1000);

        Calendar ca2 = Calendar.getInstance();
        // 每天定点执行
        ca2.set(Calendar.HOUR_OF_DAY, 22);
        ca2.set(Calendar.MINUTE, 00);
        ca2.set(Calendar.SECOND, 00);

        Timer timer2=new Timer();
        timer2.schedule(new TimerTask(){
            public void run(){
                SGlogger.info("Timer Start");
                new ShuaigayFetcher().doSocialGameGrab();
            }
        },ca2.getTime(),24 * 60 * 60 * 1000);
    }
}
