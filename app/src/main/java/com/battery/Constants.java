package com.battery;

/**
 * Created by szl on 2018/1/31.
 * 保存常用常量的常量类
 */

public class Constants {

//    public static final String NETWORKIP = "http://192.168.0.8:8080"; // 服务器的IP地址
    public static final String NETWORKIP = "http://118.24.0.184:8080/BatteryServer"; // 服务器的IP地址
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
    public static final String COMPLETEADDRESS = NETWORKIP + "/complete"; // 询问预约是否完成的地址
    public static final String USERVEHICLE = NETWORKIP + "/user_vehicle"; // 根据用户id请求车辆id地址
    public static final String RECOMMENDADDRESS = NETWORKIP + "/recommend"; // 获取推荐电站地址
    public static final String REFERENCEADDRESS = NETWORKIP + "/reference"; // 获取参考车辆数据的对象
    public static final String STATIONTOUSER = NETWORKIP + "/station_to_user"; // 根据用户id电站id获取对应数据
    public static final String CANCELAPPOINTMENT = NETWORKIP + "/cancel_appointment"; // 根据用户id取消预约
    public static final String HANDLECOLLECTION = NETWORKIP + "/handle_collection"; // 根据用户id电站id收藏或取消收藏
    public static final String ELECTRICITYADDRESS = NETWORKIP + "/electricity"; // 询问电量地址
    public static final String CHANGEREFERENCE = NETWORKIP + "/change_reference"; // 更改参考车辆地址
}
