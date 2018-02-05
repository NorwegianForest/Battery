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
 * Created by szl on 2018/1/31.
 * 描述电池
 * 注释中带星号*的参数表示在数据中有对应的字段
 */

public class Battery {

    private int id; // 与数据库对应的id *
    private String number; // 电池的唯一编号 *
    private String model; // 电池的型号 *
    private int vehicleId; // 搭载电池的硬件id *
    private int stationId; // 储存电池的电站id *
    private double electricity; // 剩余电量百分比 *
    private double ratedCapacity; // 额定容量 *
    private double actualCapacity; // 实际容量 *
    private double residualCapacity; // 剩余容量 *
    private String date; // 投入使用的日期 *

    public void loadByVehicleId() {
        RequestBody body = new FormBody.Builder()
                .add("vehicle_id", Integer.toString(vehicleId)).build();
        sendRequest(Constants.BATTERYADDRESS, body);
    }

    public void load() {
        RequestBody body = new FormBody.Builder()
                .add("battery_id", Integer.toString(id)).build();
        sendRequest(Constants.BATTERYBYIDADDRESS, body);
    }

    private void sendRequest(String address, RequestBody body) {
         HttpUtil.sendRequest(address, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    Battery battery = new Gson().fromJson(responseData, Battery.class);
                    id = battery.getId();
                    number = battery.getNumber();
                    model = battery.getModel();
                    vehicleId = battery.getVehicleId();
                    stationId = battery.getStationId();
                    electricity = battery.getElectricity();
                    ratedCapacity = battery.getRatedCapacity();
                    actualCapacity = battery.getActualCapacity();
                    residualCapacity = battery.getResidualCapacity();
                    date = battery.getDate();
                    Log.d("Battery", "电量:" + electricity);
                }
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }

    public double getRatedCapacity() {
        return ratedCapacity;
    }

    public void setRatedCapacity(double ratedCapacity) {
        this.ratedCapacity = ratedCapacity;
    }

    public double getActualCapacity() {
        return actualCapacity;
    }

    public void setActualCapacity(double actualCapacity) {
        this.actualCapacity = actualCapacity;
    }

    public double getResidualCapacity() {
        return residualCapacity;
    }

    public void setResidualCapacity(double residualCapacity) {
        this.residualCapacity = residualCapacity;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
