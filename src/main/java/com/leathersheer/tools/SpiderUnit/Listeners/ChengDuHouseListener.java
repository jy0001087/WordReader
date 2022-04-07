package com.leathersheer.tools.SpiderUnit.Listeners;

import org.apache.ibatis.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ChengDuHouseListener implements ServletContextListener {
    public static final Logger CDListnerLogger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        CDListnerLogger.info("CDListner start!");

        Calendar cal = Calendar.getInstance();
        // 每天定点执行
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);

        Timer timer=new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                CDListnerLogger.info("Timer Start");
                new ChengDuHouseStatement().doGrab("https://www.cdzjryb.com/");
            }
        },cal.getTime(),24 * 60 * 60 * 1000);
    }
}
