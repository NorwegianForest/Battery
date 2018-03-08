package com.battery;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

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
 * 描述用户
 * 注释中带星号*的参数表示在数据中有对应的字段
 */

public class User extends DataSupport {

    private int id; // 与数据库对应的id *
    private String phone; // 手机号码 *
    private String password; // 密码 *
    private double balance; // 账户余额 *
    private int isDefault; // 是否默认登录，1为是，0为否
    private List<Vehicle> vehicleList; // 用户所有爱车
    private List<Appointment> appointmentList; // 用户所有预约记录
    private boolean isAppointment = false; // 该用户是否已有一次未完成预约

    /**
     * 使用OkHttp向服务器发送登录请求，并返回完整User信息
     * @param latch 用于等待线程结束，登录操作必须等待
     */
    public void login(final CountDownLatch latch) {
        RequestBody body = new FormBody.Builder()
                .add("phone", phone)
                .add("password", password).build();
        HttpUtil.sendRequest(Constants.LOGINADDRESS, body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("illegal")) {
                    Log.d("User", phone+"登录失败");
                } else {
                    User user = new Gson().fromJson(responseData, User.class);
                    id = user.getId();
                    phone = user.getPhone();
                    password = user.getPassword();
                    balance = user.getBalance();
                    Log.d("User", phone+"登录成功");
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
    }

    /**
     * 根据用户id向服务器请求所有爱车数据
     * @param latch 用于等待线程结束
     */
    public void loadVehicle(final CountDownLatch latch) {
        RequestBody body = new FormBody.Builder()
                .add("user_id", Integer.toString(id)).build();
        HttpUtil.sendRequest(Constants.USERVEHICLE, body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    List<UserVehicle> uvList = new Gson().fromJson(responseData
                            , new TypeToken<List<UserVehicle>>(){}.getType());
                    vehicleList = new ArrayList<>();
                    // 等待Vehicle对象根据id完善自身其他数据
                    CountDownLatch l1 = new CountDownLatch(uvList.size());
                    for (UserVehicle uv : uvList) {
                        Vehicle vehicle = new Vehicle();
                        vehicle.setId(uv.getVehicleId());
                        Log.d("User", "获取车辆id:" + vehicle.getId());
                        vehicle.load(l1);
                        vehicleList.add(vehicle);
                    }

                    try {
                        l1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
    }

    /**
     * 根据用户id，向服务器请求所有预约信息
     */
    public void loadAppointment(final CountDownLatch latch) {
        RequestBody body = new FormBody.Builder()
                .add("user_id", Integer.toString(id)).build();
        HttpUtil.sendRequest(Constants.APPOINTMENTADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                appointmentList = new Gson().fromJson(responseData, new TypeToken<List<Appointment>>(){}.getType());
                isAppointment = false;
                for (Appointment appointment : appointmentList) {
                    appointment.loadStation();
                    appointment.loadNewBattery();
                    if (appointment.getComplete() == 0) {
                        isAppointment = true;
                    }
                    Log.d("User:", "预约时间:" +appointment.getDate());
                    Log.d("User:", "是否完成:" + appointment.getComplete());
                    Log.d("User:", "被换电车辆:" + appointment.getVehicleId());
                }
                latch.countDown();
            }
        });
    }

    /**
     * 向服务器询问预约是否已完成
     */
    public void isComplete() {
        RequestBody body = new FormBody.Builder()
                .add("id", Integer.toString(id)).build();
        HttpUtil.sendRequest(Constants.COMPLETEADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("未完成")) {

                } else {
                    isAppointment = false;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public boolean isAppointment() {
        return isAppointment;
    }

    public void setAppointment(boolean appointment) {
        isAppointment = appointment;
    }
}
