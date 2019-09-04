package com.leathersheer.tools.WordWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WordWorker implements Workers {
    public static final Logger workerLogger = LogManager.getLogger();
    public static void main(String[] args){
        System.out.println("test  ok");
        workerLogger.trace("logger start");
    }
}
