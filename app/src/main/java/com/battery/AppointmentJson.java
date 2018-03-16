package com.battery;

/**
 * Created by szl on 2018/2/4.
 * 用于在预约中接收预约结果
 * 已弃用
 */

public class AppointmentJson {

    private int appointment_id; // 预约记录id
    private int batteryId; // 预约的电池id
    private String date; // 预约的时间

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

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
