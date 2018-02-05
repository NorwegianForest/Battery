package com.battery;

/**
 * Created by szl on 2018/1/31.
 * 保存常用常量的常量类
 */

public class Constants {

    public static final String NETWORKIP = "http://192.168.0.5:8080"; // 服务器的IP地址
    public static final String LOGINADDRESS = NETWORKIP + "/login"; // 登录的地址
    public static final String STATIONADDRESS = NETWORKIP + "/station"; // 获取电站信息的地址
    public static final String VEHICLEADDRESS = NETWORKIP + "/vehicle"; // 获取车辆信息地址
    public static final String BATTERYADDRESS = NETWORKIP + "/battery"; // 根据车辆id获取电池信息地址
    public static final String RECORDADDRESS = NETWORKIP + "/record"; // 获取换电记录的地址
    public static final String STATIONBYIDADDRESS = NETWORKIP + "/station_by_id"; // 根据电站id获取电站信息的地址
    public static final String BATTERYBYIDADDRESS = NETWORKIP + "/battery_by_id"; // 根据电池id获取电池信息的地址
    public static final String APPOINTMENTADDRESS = NETWORKIP + "/appointment"; // 获取用户预约信息的地址
    public static final String COLLECTIONADDRESS = NETWORKIP + "/collection"; // 获取用户收藏信息的地址
    public static final String HANDLEAPPOINTMENT = NETWORKIP + "/handle_appointment"; // 请求预约的地址
    public static final String COMPLETEADDRESS = NETWORKIP + "/complete"; // 请求预约的地址
    public static final String USERVEHICLE = NETWORKIP + "/user_vehicle"; // 根据用户id请求车辆id地址

}
