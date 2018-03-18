package com.battery;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

/**
 * Created by szl on 2018/2/2.
 * 通过LitePal对数据库进行操作
 */

public class Database {

    public static void initDatabase() {
        LitePal.getDatabase();
    }

    /**
     * 创建默认用户，手机号18402884427，密码123456
     * 如果找到默认用户则不进行任何操作，如果未找到则添加默认用户
     */
    public static void initUser() {
        User alreadyUser = DataSupport.where("phone = ?", "18402884427").findFirst(User.class);
        if (alreadyUser == null) {
            Log.d("Database", "not found");
            LitePal.getDatabase();
            setNoDefault();
            User user = new User();
            user.setPhone("18402884427");
            user.setPassword("123456");
            user.setIsDefault(1);
            user.save();
        }
    }

    /**
     * 获取本地数据库存储的默认登录用户的手机号密码
     * @return 本地用户对象
     */
    public static User getLocalUser() {
        return DataSupport.where("isDefault = ?", "1").findFirst(User.class);
    }

    public static void setNoDefault() {
        User user = new User();
        user.setToDefault("isDefault");
        user.updateAll();
    }

    public static void setNewDefault(String phone, String password, int isDefault) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setIsDefault(isDefault);
        user.save();
    }
}
