package com.example.lenovo.battery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.Station;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StationActivity extends AppCompatActivity {

    TextView address;
    TextView name;
    TextView queueTime;
    TextView distance;
    Button appointmentButton;

    MenuItem item;

    String userId;
    String stationId;
    Station station;

    Handler handler;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        MainActivity.setStatusBarColor(this);

        name = findViewById(R.id.station_details_name);
        address = findViewById(R.id.station_details_address);
        queueTime = findViewById(R.id.station_details_queueTime);
        distance = findViewById(R.id.station_details_distance);
        appointmentButton = findViewById(R.id.station_details_appointmentButton);

        Toolbar toolbar = findViewById(R.id.station_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("电站详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userId = getIntent().getStringExtra("id");
        stationId = getIntent().getStringExtra("station_id");

        // 如果userId是-1，就向station_by_id请求数据，不显示排队时间和距离，不能点击预约或收藏
        handler = new Handler();
        if (userId.equals("-1")) {
            loadStationById();

            dialog = new AlertDialog.Builder(StationActivity.this);
            dialog.setTitle("请先登录");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(StationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        } else {
            loadStationToUser();
        }

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 预约按钮可预约和取消预约
                if (!userId.equals("-1")) {
                    if (appointmentButton.getText().equals("预约")) {
                        sendAppointment();
                    } else {
                        cancelAppointment();
                    }
                } else {
                    dialog.show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_toolbar, menu);
        item = menu.findItem(R.id.star);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.star:
                if (!userId.equals("-1")) {
                    this.item = item;
                    if (item.getTitle().equals("收藏")) {
                        setCollection();
                    } else {
                        cancelCollection();
                    }
                } else {
                    dialog.show();
                }
                break;
                default:
        }
        return true;
    }

    private void loadStationById() {
        RequestBody body = new FormBody.Builder().add("station_id", stationId).build();
        HttpUtil.sendRequest(Constants.STATIONBYIDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                station = new Gson().fromJson(responseData, Station.class);
                new Thread() {
                    @Override
                    public void run() {
                        handler.post(runStationById);
                    }
                }.start();
            }
        });
    }

    Runnable runStationById = new Runnable() {
        @Override
        public void run() {
            name.setText(station.getName());
            address.setText(station.getAddress());
        }
    };

    private void loadStationToUser() {
        RequestBody body = new FormBody.Builder().add("user_id", userId)
                .add("station_id", stationId).build();
        HttpUtil.sendRequest(Constants.STATIONTOUSER, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                station = new Gson().fromJson(responseData, Station.class);
                new Thread() {
                    @Override
                    public void run() {
                        handler.post(runStation);
                    }
                }.start();
            }
        });
    }

    Runnable runStation = new Runnable() {
        @Override
        public void run() {

            name.setText(station.getName());
            address.setText(station.getAddress());
            String timeStr = "排队时间:" + station.getQueueTime() + "min";
            queueTime.setText(timeStr);
            String distanceStr = "距离我的位置:" + station.getDistance() + "km";
            distance.setText(distanceStr);

            if (station.isAppointment()) {
                appointmentButton.setText("取消预约");
                appointmentButton.setBackgroundColor(Color.rgb(205, 220, 57));
            }

            if (station.isCollection()) {
                item.setTitle("取消收藏");
                item.setIcon(R.drawable.ic_star_white_36dp);
            }
        }
    };

    private void snackBarResult(String result) {
        Snackbar.make(findViewById(R.id.station), result, Snackbar.LENGTH_SHORT).show();
    }

    private void sendAppointment() {
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId).add("station_id", stationId).build();
        HttpUtil.sendRequest(Constants.HANDLEAPPOINTMENT, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("预约失败")) {
                    snackBarResult("预约失败");
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runSendAppointment);
                        }
                    }.start();
                }
            }
        });
    }

    Runnable runSendAppointment = new Runnable() {
        @Override
        public void run() {
            snackBarResult("预约成功");
            appointmentButton.setText("取消预约");
            appointmentButton.setBackgroundColor(Color.rgb(205, 220, 57));
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
            appointmentButton.setText("预约");
            appointmentButton.setBackgroundColor(Color.rgb(0, 150, 136));
        }
    };

    Runnable runCancelAppointmentFailed = new Runnable() {
        @Override
        public void run() {
            snackBarResult("预约取消失败");
        }
    };

    private void setCollection() {
        RequestBody body = new FormBody.Builder().add("user_id", userId)
                .add("station_id", stationId)
                .add("method", "set").build();
        HttpUtil.sendRequest(Constants.HANDLECOLLECTION, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("收藏成功")) {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runSetCollection);
                        }
                    }.start();
                }
            }
        });
    }

    Runnable runSetCollection = new Runnable() {
        @Override
        public void run() {
            item.setIcon(R.drawable.ic_star_white_36dp);
            item.setTitle("取消收藏");
            snackBarResult("收藏成功");
        }
    };

    private void cancelCollection() {
        RequestBody body = new FormBody.Builder().add("user_id", userId)
                .add("station_id", stationId)
                .add("method", "cancel").build();
        HttpUtil.sendRequest(Constants.HANDLECOLLECTION, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("取消收藏成功")) {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runCancelCollection);
                        }
                    }.start();
                }
            }
        });
    }

    Runnable runCancelCollection = new Runnable() {
        @Override
        public void run() {
            item.setIcon(R.drawable.ic_star_border_white_36dp);
            item.setTitle("收藏");
            snackBarResult("已取消收藏");
        }
    };

    /**
     * 转跳到导航界面
     * @param view action_navigation.xml
     */
    public void navigate(View view) {
        Intent intent = new Intent(StationActivity.this, NavigationActivity.class);
        intent.putExtra("id", userId);
        intent.putExtra("station_id", stationId);
        startActivity(intent);
    }
}
