package com.leathersheer.tools.SpiderUnit.Listeners.DBUnits;


import java.util.Date;

public class ResoldHouseBean {
    public String houseid;    //从url里分离出来的ID
    public double proportion;   //面积
    public double totalPrice;
    public double unitPrice;
    public String houseType;   //户型
    public String mansion;  // 楼型
    public String floor;
    public String orientation;  //朝向
    public String isSold;        //已售标志
    public String houseUrl;
    public String age;       //房龄
    public Date grabTime;    //抓取时间
    public String source;    //信息来源网站
}
