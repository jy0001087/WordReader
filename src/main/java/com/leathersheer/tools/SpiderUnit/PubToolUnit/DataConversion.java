package com.leathersheer.tools.SpiderUnit.PubToolUnit;

import java.sql.Timestamp;

public class DataConversion {
    public static Timestamp getCurrentTimeStamp(){
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }
}
