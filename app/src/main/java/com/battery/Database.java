package com.battery;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

/**
 * Created by szl on 2018/2/2.
 * 通过LitePal对数据库进行操作
 */

public class Database {

    /**
     * 创建默认用户，手机号18402884427，密码123456
     */
    public static void initUser() {
        User alreadyUser = DataSupport.where("phone = ?", "18402884427").findFirst(User.class);
        if (alreadyUser == null) {
            Log.d("Database", "not found");
            LitePal.getDatabase();
            User user = new User();
            user.setPhone("18402884427");
            user.setPassword("123456");
            user.setIsDefault(1);
            user.save();
        }
    }

    /**
     * 获取本地数据库存储的用户的手机号密码
     * @return 本地用户对象
     */
    public static User getLocalUser() {
        User localUser = DataSupport.where("isDefault = ?", "1").findFirst(User.class);
        return localUser;
    }
}
