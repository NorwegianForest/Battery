package com.example.lenovo.battery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.battery.Appointment;
import com.battery.Constants;
import com.battery.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentActivity extends AppCompatActivity {

    private String userId;

    private List<Appointment> appointmentList;
    private Appointment appointment;

    private TextView name;
    private TextView address;
    private TextView time;
    private TextView completeTime;
    private TextView distance;

    private Button cancelButton;

    private com.battery.Station station;
    private Handler handler;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        // 设置统一的状态栏颜色
        MainActivity.setStatusBarColor(this);

        // 工具栏设定
        Toolbar toolbar = findViewById(R.id.appointment_toolbar);
        toolbar.setTitle("预约信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(AppointmentActivity.this);
        progressDialog.setTitle("加载预约信息");
        progressDialog.setMessage("请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        name = findViewById(R.id.appointment_name);
        address = findViewById(R.id.appointment_address);
        time = findViewById(R.id.appointment_time);
        completeTime = findViewById(R.id.appointment_complete_time);
        distance = findViewById(R.id.appointment_distance);
        cancelButton = findViewById(R.id.appointment_cancel);

        handler = new Handler();

        // 获取上级活动参数 用户id
        String id = getIntent().getStringExtra("id");
        userId = id;
        loadAppointment(id);

        // 取消预约按钮监听事件
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelButton.getText().equals("取消预约")) {
                    cancelAppointment();
                }
            }
        });
    }

    /**
     * 加载用户的当前预约信息并显示
     * @param id 用户id
     */
    private void loadAppointment(String id) {
        RequestBody body = new FormBody.Builder().add("user_id", id).build();
        HttpUtil.sendRequest(Constants.APPOINTMENTADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                // 获取到的是该用户的所有预约信息，无论是当前的、已完成的或已取消的
                appointmentList = new Gson().fromJson(responseData, new TypeToken<List<Appointment>>(){}.getType());

                // Appointment对象中只有station_id和new_battery_id属性，没有对应的电站名和电池编号属性
                // 故需要调用加载电站和电池对象的方法
                for (Appointment appointment : appointmentList) {
                    appointment.loadStation();
                    appointment.loadNewBattery();
                }

                boolean hasAppointment = false;
                // 在用户的所有预约数据中找到当前正在预约的一条，有且仅有一条
                for (Appointment a : appointmentList) {
                    if (a.getComplete() == 0) {
                        hasAppointment = true;

                        // 根据station_id属性向服务器请求数据并加载晚上Station对象数据，再加以显示
                        // 电站的信息和预约的信息分开是由于上一个for循环可能线程没有结束
                        // 导致在还没有请求到数据的情况下就执行了更新界面显示的语句而显示错误
                        loadStation(Integer.toString(a.getStationId()));

                        appointment = a;

                        // 更新UI的语句一般情况下需要在单独的线程执行，否则可能会出现错误
                        new Thread(){
                            public void run(){
                                handler.post(setAppointment);
                            }
                        }.start();
                    }
                }

                if (!hasAppointment) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(AppointmentActivity.this, NoAppointmentActivity.class);
                    startActivity(intent);
                    AppointmentActivity.this.finish();
                }
            }
        });
    }

    /**
     * UI更新预约信息的的线程
     */
    Runnable setAppointment = new  Runnable(){
        @Override
        public void run() {
            String timeStr = "预约时间: " + appointment.getDate().split(" ")[1];
            System.out.println("AppointmentActivity:" + appointment.getDate());
            time.setText(timeStr);
            timeStr = "排队时间: " + Integer.toString(appointment.getTime()) + "min";
            System.out.println("AppointmentActivity:" + appointment.getTime());
            completeTime.setText(timeStr);
            String distanceStr = "距离我的位置: " + Double.toString(appointment.getDistance()) + "km";
            System.out.println("AppointmentActivity:" + appointment.getDistance());
            distance.setText(distanceStr);

            progressDialog.dismiss();
        }
    };

    /**
     * 加载用户当前预约的电站的数据，并更新UI
     * @param stationId 电站id
     */
    private void loadStation(String stationId) {
        RequestBody body = new FormBody.Builder().add("station_id", stationId).build();
        HttpUtil.sendRequest(Constants.STATIONBYIDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    station = new Gson().fromJson(responseData, com.battery.Station.class);
                    new Thread(){
                        public void run(){
                            handler.post(setStation);
                        }
                    }.start();
                }
            }
        });
    }

    /**
     * UI更新电站名、电站地址的线程
     */
    Runnable setStation = new  Runnable(){
        @Override
        public void run() {
            name.setText(station.getName());
            address.setText(station.getAddress());
        }
    };

    /**
     * 取消用户当前预约，在“取消预约”按钮事件中调用
     */
    private void cancelAppointment() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.CANCELAPPOINTMENT, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                // 一般情况下不会取消失败
                if (responseData.equals("取消预约成功")) {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runCancelAppointment);
                        }
                    }.start();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runCancelAppointmentFailed);
                        }
                    }.start();
                }
            }
        });
    }

    /**
     * 成功取消预约后，更新UI的线程
     */
    Runnable runCancelAppointment = new Runnable() {
        @Override
        public void run() {
            // 显示提示
            snackBarResult("预约已取消");

            // 更新“取消预约”按钮的UI
            cancelButton.setText("已取消");
            cancelButton.setBackgroundColor(Color.rgb(213, 213, 213));
            cancelButton.setTextColor(Color.rgb(170,170,170));
        }
    };

    /**
     * 取消预约失败后的UI提示线程
     */
    Runnable runCancelAppointmentFailed = new Runnable() {
        @Override
        public void run() {
            snackBarResult("预约取消失败");
        }
    };

    /**
     * 布局低端显示提示信息，使用snackBar
     * @param result 要显示的提示信息
     */
    private void snackBarResult(String result) {
        Snackbar.make(findViewById(R.id.appointment), result, Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    /**
     * 转跳到导航界面
     * @param view action_navigation.xml
     */
    public void navigate(View view) {
        Intent intent = new Intent(AppointmentActivity.this, NavigationActivity.class);
        intent.putExtra("id", userId);
        intent.putExtra("station_id", Integer.toString(appointment.getStation().getId()));
        startActivity(intent);
    }
}
