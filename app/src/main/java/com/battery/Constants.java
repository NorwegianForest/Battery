package com.battery;

/**
 * Created by szl on 2018/1/31.
 * 保存常用常量的常量类
 */

public class Constants {

    public static final String NETWORKIP = "http://192.168.0.8:8080"; // 服务器的IP地址
    public static final String LOGINADDRESS = NETWORKIP + "/login"; // 登录的地址
    public static final String STATIONADDRESS = NETWORKIP + "/station"; // 获取电站信息的地址
    public static final String VEHICLEADDRESS = NETWORKIP + "/vehicle"; // 获取车辆信息地址
    public static final String BATTERYADDRESS = NETWORKIP + "/battery"; // 获取电池信息地址

}
