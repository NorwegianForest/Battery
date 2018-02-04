package com.battery;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by szl on 2018/1/31.
 * 描述车载硬件设备
 * 注释中带星号*的参数表示在数据中有对应的字段
 */

public class Vehicle {

    private int id; // 与数据库对应的id *
    private String number; // 硬件设备唯一的编号 *
    private String brand; // 汽车的品牌 *
    private String model; // 汽车的型号 *
    private String plate; // 汽车的车牌号码 *
    private User admin; // 硬件设备的管理员用户 *
    private double longitude; // 经度 *
    private double latitude; // 纬度 *
    private String date; // 投入使用的时间 *
    private Battery battery; // 汽车搭载的电池
    private double life; // 电池的续航里程

    /**
     * 根据电池id，向服务器请求车辆搭载的电池信息
     */
    public void loadBattery() {
        RequestBody body = new FormBody.Builder()
                .add("id", Integer.toString(id)).build();
        HttpUtil.sendRequest(Constants.BATTERYADDRESS, body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                battery = new Gson().fromJson(responseData, Battery.class);
                Log.d("Vehicle:", "id:"+battery.getId());
                Log.d("Vehicle:", "编号:" + battery.getNumber());
                Log.d("Vehicle:", "型号:"+battery.getModel());
                Log.d("Vehicle:", "电量:"+battery.getElectricity());
                Log.d("Vehicle:", "额定容量:"+battery.getRatedCapacity());
                Log.d("Vehicle:", "实际容量:"+battery.getActualCapacity());
                Log.d("Vehicle:", "剩余容量:"+battery.getResidualCapacity());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String getNumber() {
        return number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
