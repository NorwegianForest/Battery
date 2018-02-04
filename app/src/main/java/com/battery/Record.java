package com.battery;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

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
        RequestBody body = new FormBody.Builder()
                .add("id", Integer.toString(stationId)).build();
        HttpUtil.sendRequest(Constants.STATIONBYIDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                station = new Gson().fromJson(responseData, Station.class);
                Log.d("Record", "电站名:"+station.getName());
            }
        });
    }

    /**
     * 根据电池id，向服务器请求旧电池数据
     */
    public void loadOldBattery() {
        RequestBody body = new FormBody.Builder()
                .add("id", Integer.toString(oldBatteryId)).build();
        HttpUtil.sendRequest(Constants.BATTERYBYIDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                oldBattery = new Gson().fromJson(responseData, Battery.class);
                Log.d("Record", "旧电池:"+oldBattery.getNumber());
            }
        });
    }

    /**
     * 根据电池id，向服务器请求新电池数据
     */
    public void loadNewBattery() {
        RequestBody body = new FormBody.Builder()
                .add("id", Integer.toString(newBatteryId)).build();
        HttpUtil.sendRequest(Constants.BATTERYBYIDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                newBattery = new Gson().fromJson(responseData, Battery.class);
                Log.d("Record", "新电池:"+newBattery.getNumber());
            }
        });
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
