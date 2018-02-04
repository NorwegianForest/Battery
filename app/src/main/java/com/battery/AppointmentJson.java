package com.battery;

/**
 * Created by szl on 2018/2/4.
 * 用于在预约中接收预约结果
 */

public class AppointmentJson {

    private int batteryId; // 预约的电池id
    private String date; // 预约的时间

    public int getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(int batteryId) {
        this.batteryId = batteryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
