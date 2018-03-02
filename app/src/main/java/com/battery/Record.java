package com.battery;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by szl on 2018/2/4.
 * 描述换电记录
 * 注释中带星号*的参数表示在数据中有对应的字段
 */

public class Record {

    private int id; // 数据库自增id *
    private int userId; // 执行换电的用户id *
    private int vehicleId; // 被换电的车辆id *
    private int stationId; // 提供换电的电站id *
    private double money; // 费用 *
    private int oldBatteryId; // 旧电池id *
    private int newBatteryId; // 新电池id *
    private String date; // 完成时间 *
    private Station station; // 提供换电的电站对象
    private Battery oldBattery; // 旧电池对象
    private Battery newBattery; // 新电池对象

    /**
     * 根据电站id向服务器请求该电站的数据
     */
    public void loadStation() {
        station = new Station();
        station.setId(stationId);
        CountDownLatch latch = new CountDownLatch(1);
        station.load(latch);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据电池id，向服务器请求旧电池数据
     */
    public void loadOldBattery() {
        oldBattery = new Battery();
        oldBattery.setId(oldBatteryId);
        CountDownLatch latch = new CountDownLatch(1);
        oldBattery.load(latch);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据电池id，向服务器请求新电池数据
     */
    public void loadNewBattery() {
        newBattery = new Battery();
        newBattery.setId(newBatteryId);
        CountDownLatch latch = new CountDownLatch(1);
        newBattery.load(latch);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getOldBatteryId() {
        return oldBatteryId;
    }

    public void setOldBatteryId(int oldBatteryId) {
        this.oldBatteryId = oldBatteryId;
    }

    public int getNewBatteryId() {
        return newBatteryId;
    }

    public void setNewBatteryId(int newBatteryId) {
        this.newBatteryId = newBatteryId;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Battery getOldBattery() {
        return oldBattery;
    }

    public void setOldBattery(Battery oldBattery) {
        this.oldBattery = oldBattery;
    }

    public Battery getNewBattery() {
        return newBattery;
    }

    public void setNewBattery(Battery newBattery) {
        this.newBattery = newBattery;
    }
}
