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
