package com.battery;

/**
 * Created by szl on 2018/2/5.
 * 对应数据库中的user_vehicle数据表
 * 带星号*的属性表示数据库中有对应字段
 */

public class UserVehicle {

    private int id; // 数据库自增id *
    private int userId; // 用户id *
    private int vehicleId; // 车辆id *
    private int admin; // 该用户是否为该车辆的管理员，是为1，否为0 *

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

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
}
