package com.example.lenovo.battery;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.UserVehicle;
import com.battery.Vehicle;
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

public class MyVehicleActivity extends AppCompatActivity {

    private List<Vehicle> vehicleList;
    RecyclerView recyclerView;
    Handler handler;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle);

        // 设置统一的状态栏颜色
        MainActivity.setStatusBarColor(this);

        Toolbar toolbar = findViewById(R.id.my_vehicle_toolbar);
        toolbar.setTitle("我的爱车");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.my_vehicle_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        handler = new Handler();
        userId = getIntent().getStringExtra("id");
        loadVehicle(userId);
    }

    /**
     * 获取用户的所有车辆数据,并更新UI
     * @param id 用户id
     */
    private void loadVehicle(String id) {
        RequestBody body = new FormBody.Builder().add("user_id", id).build();
        HttpUtil.sendRequest(Constants.USERVEHICLE, body, new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    // 服务器响应的数据仅仅是用户对应的所有车辆的id
                    // 需要再通过车辆id完善车辆的其他数据
                    List<UserVehicle> uvList = new Gson().fromJson(responseData
                            , new TypeToken<List<UserVehicle>>(){}.getType());

                    vehicleList = new ArrayList<>();

                    // 等待Vehicle对象根据id完善自身其他数据
                    CountDownLatch l1 = new CountDownLatch(uvList.size());
                    for (UserVehicle uv : uvList) {
                        Vehicle vehicle = new Vehicle();
                        vehicle.setId(uv.getVehicleId());

                        // 该Vehicle对象通过id请求其他数据，包括对应的电池数据
                        vehicle.load(l1);
                        vehicleList.add(vehicle);
                    }

                    try {
                        l1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runLoadVehicle);
                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * UI通过重新设置配适器,更新车辆数据的线程
     */
    Runnable runLoadVehicle = new Runnable() {
        @Override
        public void run() {
            VehicleAdapter adapter = new VehicleAdapter(vehicleList, userId);
            recyclerView.setAdapter(adapter);
        }
    };
}
