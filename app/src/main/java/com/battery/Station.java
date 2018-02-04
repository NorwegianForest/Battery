package com.battery;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
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

    /**
     * 根据id向服务器请求所有电站信息
     * @param userId 用户id
     * @param vehicleId 用于计算距离的参考车辆id
     * @param latch 用于保证线程已结束
     */
    public void load(int userId, int vehicleId, final CountDownLatch latch) {
        RequestBody body = new FormBody.Builder()
                .add("user_id", Integer.toString(userId))
                .add("vehicle_id", Integer.toString(vehicleId)).build();
        HttpUtil.sendRequest(Constants.STATIONADDRESS, body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                stationList = new Gson().fromJson(responseData,
                        new TypeToken<List<Station>>(){}.getType());
                for (Station station : stationList) {
                    Log.d("Station", "id:"+station.getId());
                    Log.d("Station", "名称:"+station.getName());
                    Log.d("Station", "地址:"+station.getAddress());
                    Log.d("Station", "经度:"+station.getLongitude());
                    Log.d("Station", "纬度:"+station.getLatitude());
                    Log.d("Station", "距离:"+station.getDistance());
                    Log.d("Station", "排队时间:"+station.getQueueTime());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
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
}
