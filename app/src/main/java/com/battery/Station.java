package com.battery;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by szl on 2018/1/31.
 * 描述电站
 * 注释中带星号*的参数表示在数据中有对应的字段
 */

public class Station {

    private int id; // 与数据库对应的id *
    private String name; // 电站名称 *
    private String address; // 电站详细地址 *
    private double longitude; // 经度 *
    private double latitude; // 纬度 *
    private double distance; // 距离
    private int queueTime; // 排队时间
    private List<Station> stationList; // 电站列表
    private boolean isAppointment; // 是否已经预约，针对确知用户id的情况
    private boolean isCollection; // 是否已经收藏，针对确知用户id的情况

    /**
     * 根据电站id向服务器请求完善自身数据
     */
    public void load(final CountDownLatch latch) {
        RequestBody body = new FormBody.Builder()
                .add("station_id", Integer.toString(id)).build();
        HttpUtil.sendRequest(Constants.STATIONBYIDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    Station station = new Gson().fromJson(responseData, Station.class);
                    id = station.getId();
                    name = station.getName();
                    address = station.getAddress();
                    longitude = station.getLongitude();
                    latitude = station.getLatitude();
                    Log.d("Station", "电站名称:" + name);
                }
                latch.countDown();
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(int queueTime) {
        this.queueTime = queueTime;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAppointment() {
        return isAppointment;
    }

    public void setAppointment(boolean appointment) {
        isAppointment = appointment;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }
}
