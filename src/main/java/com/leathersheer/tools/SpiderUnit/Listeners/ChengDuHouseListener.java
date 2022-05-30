package com.leathersheer.tools.SpiderUnit.Listeners;

import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesReader;
import org.apache.ibatis.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ChengDuHouseListener implements ServletContextListener {
    public static final Logger CDListnerLogger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        ServletContext context=sce.getServletContext();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                CDListnerLogger.info("WholeCDHouseState Listner is alive!");
                try {
                    PropertiesReader.Builder builder= new PropertiesReader.Builder(context,"properties.json");
                    PropertiesReader pr = builder.setPropertyPath("WholeCDHouseState|initiateurl")
                            .setPropertyPath("WholeCDHouseState|executeTime")
                            .build();
                    String executeTime=pr.getProperty("executeTime");
                    String url = pr.getProperty("initiateurl");
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date executeTimeDate = df.parse(executeTime);
                    Date now = df.parse(df.format(new Date()));
                    if(now.after(executeTimeDate)){
                        CDListnerLogger.info("WholeCDHouseState Listner is executed!");
                        new ChengDuHouseStatement().doGrab(url);
                    }
                }catch (ParseException e){
                    CDListnerLogger.error("转换时间出现异常：",e);
                }
            }
        },0,2, TimeUnit.HOURS);
    }
}
