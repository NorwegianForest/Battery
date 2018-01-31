package com.battery;

/**
 * Created by szl on 2018/1/31.
 * 描述电池
 */

public class Battery {
    private int id; // 与数据库对应的id
    private String number; // 电池的唯一编号
    private String model; // 电池的型号
    private double electricity; // 剩余电量百分比
    private double ratedCapacity; // 额定容量
    private double actualCapacity; // 实际容量
    private double residualCapacity; // 剩余容量
}
