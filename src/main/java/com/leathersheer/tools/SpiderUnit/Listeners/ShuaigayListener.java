package com.leathersheer.tools.SpiderUnit.Listeners;

import com.leathersheer.tools.SpiderUnit.PubToolUnit.PropertiesReader;
import com.leathersheer.tools.SpiderUnit.Shuaigay.ShuaigayFetcher;
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

public class ShuaigayListener implements ServletContextListener {

    public static final Logger SGlogger = LogManager.getLogger();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        ServletContext context=sce.getServletContext();

        PropertiesReader.Builder builder= new PropertiesReader.Builder(context,"properties.json");
        PropertiesReader pr1 = builder.setPropertyPath("ShuaiGay|interval")
                .build();
        int interval = Integer.valueOf(pr1.getProperty("interval"));

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SGlogger.info("ShuaiGaySocialGame Listener is alive!");
                PropertiesReader.Builder builder= new PropertiesReader.Builder(context,"properties.json");
                PropertiesReader pr = builder.setPropertyPath("ShuaiGay|interval")
                        .setPropertyPath("ShuaiGay|SocialGameStartTime")
                        .setPropertyPath("ShuaiGay|SocialGameExpireTime")
                        .build();

                String startTime = pr.getProperty("SocialGameStartTime");
                String expireTime = pr.getProperty("SocialGameExpireTime");
                try {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date startTimeDate = df.parse(startTime);
                    Date expireTimeDate = df.parse(expireTime);
                    Date now = df.parse(df.format(new Date()));
                    if(now.after(startTimeDate) & now.before(expireTimeDate)){
                        SGlogger.info("ShuaiGaySocialGame Listener is executed!");
                        new ShuaigayFetcher().doSocialGameGrab(context);
                    }
                }catch (ParseException e){
                    SGlogger.error("转换时间出现异常：",e);
                }
            }
        },0,interval, TimeUnit.MINUTES);
    }
}
