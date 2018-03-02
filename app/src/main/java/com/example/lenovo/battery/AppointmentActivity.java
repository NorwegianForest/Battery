package com.example.lenovo.battery;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        MainActivity.setStatusBarColor(this);

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

        name = findViewById(R.id.appointment_name);
        address = findViewById(R.id.appointment_address);
        time = findViewById(R.id.appointment_time);
        completeTime = findViewById(R.id.appointment_complete_time);
        distance = findViewById(R.id.appointment_distance);
        cancelButton = findViewById(R.id.appointment_cancel);

        handler = new Handler();
        String id = getIntent().getStringExtra("id");
        userId = id;
        loadAppointment(id);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelButton.getText().equals("取消预约")) {
                    cancelAppointment();
                }
            }
        });
    }

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
                appointmentList = new Gson().fromJson(responseData, new TypeToken<List<Appointment>>(){}.getType());
                for (Appointment appointment : appointmentList) {
                    appointment.loadStation();
                    appointment.loadNewBattery();
                }
                for (Appointment a : appointmentList) {
                    if (a.getComplete() == 0) {
                        loadStation(Integer.toString(a.getStationId()));
                        appointment = a;
                        new Thread(){
                            public void run(){
                                handler.post(setAppointment);
                            }
                        }.start();
                    }
                }
            }
        });
    }

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
        }
    };

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

    Runnable setStation = new  Runnable(){
        @Override
        public void run() {
            name.setText(station.getName());
            address.setText(station.getAddress());
        }
    };

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

    Runnable runCancelAppointment = new Runnable() {
        @Override
        public void run() {
            snackBarResult("预约已取消");
            cancelButton.setText("已取消");
            cancelButton.setBackgroundColor(Color.rgb(213, 213, 213));
            cancelButton.setTextColor(Color.rgb(170,170,170));
        }
    };

    Runnable runCancelAppointmentFailed = new Runnable() {
        @Override
        public void run() {
            snackBarResult("预约取消失败");
        }
    };

    private void snackBarResult(String result) {
        Snackbar.make(findViewById(R.id.appointment), result, Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
