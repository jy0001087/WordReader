package com.leathersheer.tools.SpiderUnit.SpiderBeans;

import java.sql.Timestamp;

public class HouseBean {
    public String address;  //地址
    public String gdlocation ; //高德location坐标信息
    public Integer area;  //面积
    public String floor;  //楼层
    public Integer price;  //价格
    public String housetype;  //户型
    public String oriented;  //朝向
    public String house_code; //房源编号 唯一
    public Timestamp update_timestamp;  //房源更新时间
    public String houseurl; //房屋页面地址
    public Integer house_status; //房屋状态 0：新增 1：存续 9：失效

    public HouseBean(){
    }
}