package com.example.lenovo.battery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.battery.Appointment;
import com.battery.Battery;
import com.battery.Constants;
import com.battery.Database;
import com.battery.HttpUtil;
import com.battery.Record;
import com.battery.Station;
import com.battery.Vehicle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {

    private String userId;

    private Vehicle vehicle;
    private Battery batteryInVehicle;
    private Appointment appointment;
    private boolean hasAppointment;
    private Record record;
    private int count;
    private Station collectionStation;
    private Station closeStation;
    private Station timeStation;

    private CardView batteryCard;
    private CardView balanceCard;
    private CardView appointmentCard;
    private CardView recordCard;
    private CardView collectionCard;
    private CardView closeCard;
    private CardView timeCard;

    private TextView userName;
    private TextView battery;
    private TextView brand;
    private TextView mileage;
    private TextView chargingTime;
    private TextView balance;
    private TextView appointmentName;
    private TextView appointmentTime;
    private TextView appointmentQueue;
    private TextView recordName;
    private TextView recordMoney;
    private TextView recordTime;
    private TextView collectionCount;
    private TextView collectionName;
    private TextView closeName;
    private TextView closeTime;
    private TextView closeDistance;
    private TextView timeName;
    private TextView timeTime;
    private TextView timeDistance;

    private Handler handler;

    private ProgressDialog progressDialog;

    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);//将活动添加到活动收集器
        setContentView(R.layout.activity_user);

        // 设置统一的状态栏颜色
        MainActivity.setStatusBarColor(this);

        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setTitle("加载个人信息");
        progressDialog.setMessage("请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 找到控件对应的对象
        findView();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);
        Toolbar toolbar = findViewById(R.id.toolbarperson);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        NestedScrollView nestedScrollView;
        nestedScrollView = findViewById(R.id.scrollView);


        ImageView blurImageView = findViewById(R.id.h_back);
        ImageView avatarImageView = findViewById(R.id.h_head);

        // 处理图像
        Glide.with(this).load(R.drawable.profile).bitmapTransform(new BlurTransformation(this,25),new CenterCrop(this))
                .into(blurImageView);
        Glide.with(this).load(R.drawable.profile).bitmapTransform(new CropCircleTransformation(this))
                .into(avatarImageView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        collapsingToolbarLayout.setTitle("个人中心");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        userId = getIntent().getStringExtra("id");

        String name = getIntent().getStringExtra("name");
        userName.setText(name);

        String balanceStr = getIntent().getStringExtra("balance");
        balance.setText(balanceStr);

        handler = new Handler();

        if (!userId.equals("-1")) {
            // 加载各卡片的数据
            loadVehicleCard();
            loadAppointmentCard();
            loadRecordCard();
            loadCollectionCard();
        } else {
            batteryCard.setVisibility(CardView.GONE);
            balanceCard.setVisibility(CardView.GONE);
            appointmentCard.setVisibility(CardView.GONE);
            recordCard.setVisibility(CardView.GONE);
            collectionCard.setVisibility(CardView.GONE);
            progressDialog.dismiss();
        }

        loadStationCard();

        // 卡片的点击事件
        addListenerForCards();
    }

    /**
     * 获取车辆数据，并更新UI卡片
     * 该卡片只显示用户的参考车辆的信息
     */
    private void loadVehicleCard() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.REFERENCEADDRESS, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    vehicle = new Gson().fromJson(responseData, Vehicle.class);
                    loadBatteryCard();
                    new Thread(){
                        public void run(){
                            handler.post(runVehicleCard);
                        }
                    }.start();
                }
            }
        });
    }

    private void loadBatteryCard() {
        RequestBody body = new FormBody.Builder()
                .add("vehicle_id", Integer.toString(vehicle.getId())).build();
        HttpUtil.sendRequest(Constants.BATTERYADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    batteryInVehicle = new Gson().fromJson(responseData, Battery.class);
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runBattery);
                        }
                    }.start();
                }
            }
        });
    }

    Runnable runBattery = new Runnable() {
        @Override
        public void run() {
            Double rate = batteryInVehicle.getResidualCapacity() / batteryInVehicle.getActualCapacity();
            NumberFormat nFormat = NumberFormat.getPercentInstance();
            battery.setText(nFormat.format(rate));

            String mileageStr = "剩余里程: " + Double.toString(5.0 * batteryInVehicle.getResidualCapacity()) + "km";
            mileage.setText(mileageStr);

            String timeStr = "充电需要: " + (int) (4.0 * (batteryInVehicle.getActualCapacity() - batteryInVehicle.getResidualCapacity())) + "min";
            chargingTime.setText(timeStr);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_toolbar, menu);
        item = menu.findItem(R.id.login);
        if (userId.equals("-1")) {
            this.item.setTitle("登录");
        } else {
            this.item.setTitle("注销");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                this.item = item;
                if (this.item.getTitle().equals("登录")) {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if (this.item.getTitle().equals("注销")) {
                    Database.setNoDefault();
                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent);
                    ActivityCollector.finishAll();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * UI更新车辆数据卡片的线程
     */
    Runnable runVehicleCard = new  Runnable(){
        @Override
        public void run() {
            String brandStr = vehicle.getBrand() + " - " + vehicle.getModel();
            brand.setText(brandStr);

        }
    };

    /**
     * 获取预约信息，并更新UI卡片
     * 该卡片只显示用户当前的预约信息
     * 如无预约则显示 暂无预约
     */
    private void loadAppointmentCard() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.APPOINTMENTADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                List<Appointment> appointmentList = new Gson().fromJson(responseData, new TypeToken<List<Appointment>>(){}.getType());
                hasAppointment = false;
                for (Appointment appointment : appointmentList) {
                    appointment.loadStation();
                    appointment.loadNewBattery();
                }
                for (Appointment a : appointmentList) {
                    if (a.getComplete() == 0) {
                        appointment = a;
                        hasAppointment = true;
                    }
                }
                new Thread(){
                    public void run(){
                        handler.post(runAppointmentCard);
                    }
                }.start();
            }
        });
    }

    /**
     * UI更新预约数据卡片的线程
     */
    Runnable runAppointmentCard = new  Runnable() {
        @Override
        public void run() {
            if (hasAppointment) {
                appointmentName.setText(appointment.getStation().getName());
                String timeStr = "预约时间 " + appointment.getDate().split(" ")[1];
                appointmentTime.setText(timeStr);
                timeStr = "排队时长 " + Integer.toString(appointment.getTime()) + "min";
                appointmentQueue.setText(timeStr);
            } else {
                appointmentName.setText("暂无预约");
                appointmentTime.setText("");
                appointmentQueue.setText("");
            }

            progressDialog.dismiss();
        }
    };

    /**
     * 获取换电记录数据，并更新UI卡片
     * 该卡片只显示用户的最近一次换电数据
     */
    private void loadRecordCard() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.RECORDADDRESS,  body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {
                    Log.d("RecordActivity", "换电数据无结果");
                } else {
                    List<Record> recordList = new Gson().fromJson(responseData, new TypeToken<List<Record>>() {}.getType());
                    record = recordList.get(0);
                    record.loadStation();
                    record.loadOldBattery();
                    record.loadNewBattery();
                    new Thread(){
                        public void run(){
                            handler.post(runRecordCard);
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
     * UI更新换电记录数据卡片的线程
     */
    Runnable runRecordCard = new  Runnable() {
        @Override
        public void run() {
            recordName.setText(record.getStation().getName());
            String money = "¥" + record.getMoney();
            recordMoney.setText(money);
            String time = "完成时间 " + record.getDate().split(" ")[0];
            recordTime.setText(time);
        }
    };

    /**
     * 获取收藏电站数据，并更新UI卡片
     * 该卡片只显示用户的其中一个收藏电站的名称，并显示收藏电站数目
     */
    private void loadCollectionCard() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.COLLECTIONADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    List<Station> stationList = new Gson().fromJson(responseData,
                            new TypeToken<List<Station>>() {
                            }.getType());
                    count = stationList.size();
                    collectionStation = stationList.get(0);
                    new Thread() {
                        public void run() {
                            handler.post(runCollectionCard);
                        }
                    }.start();
                }
            }
        });
    }

    /**
     * UI更新收藏电站数据卡片的线程
     */
    Runnable runCollectionCard = new Runnable() {
        @Override
        public void run() {
            String countStr = "收藏电站(" + count + ")";
            collectionCount.setText(countStr);
            collectionName.setText(collectionStation.getName());
        }
    };

    private void loadStationCard() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.RECOMMENDADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                List<Station> stationList = new Gson().fromJson(responseData,
                        new TypeToken<List<Station>>(){}.getType());
                closeStation = stationList.get(0);
                timeStation = stationList.get(1);
                new Thread(){
                    public void run(){
                        handler.post(runStationCard);
                    }
                }.start();
            }
        });
    }

    /**
     * UI更新推荐电站数据卡片的线程
     */
    Runnable runStationCard = new Runnable() {
        @Override
        public void run() {
            closeName.setText(closeStation.getName());
            String timeStr = "排队时长 " + closeStation.getQueueTime() + "min";
            closeTime.setText(timeStr);
            String distanceStr = "距离我的位置 " + closeStation.getDistance() + "km";
            closeDistance.setText(distanceStr);

            timeName.setText(timeStation.getName());
            timeStr = "排队时长 " + timeStation.getQueueTime() + "min";
            timeTime.setText(timeStr);
            distanceStr = "距离我的位置 " + timeStation.getDistance() + "km";
            timeDistance.setText(distanceStr);
        }
    };

    /**
     * 为各卡片添加响应的监听器，使其能转跳到各详细页面
     */
    private void addListenerForCards() {
        batteryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, MyVehicleActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });
        balanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, BalanceActivity.class);
                intent.putExtra("id", userId);
                intent.putExtra("balance", getIntent().getStringExtra("balance"));
                startActivity(intent);
            }
        });
        appointmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, AppointmentActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });
        recordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, RecordActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });
        collectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, CollectionActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });
        closeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, StationActivity.class);
                intent.putExtra("id", userId);
                intent.putExtra("station_id", Integer.toString(closeStation.getId()));
                startActivity(intent);
            }
        });
        timeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, StationActivity.class);
                intent.putExtra("id", userId);
                intent.putExtra("station_id", Integer.toString(timeStation.getId()));
                startActivity(intent);
            }
        });
    }

    /**
     * 为所有控件声明找到其对应的对象
     */
    private void findView() {
        batteryCard = findViewById(R.id.battery_card);
        balanceCard = findViewById(R.id.balance_card);
        appointmentCard = findViewById(R.id.appointment_card);
        recordCard = findViewById(R.id.record_card);
        collectionCard = findViewById(R.id.collection_card);
        closeCard = findViewById(R.id.close_card);
        timeCard = findViewById(R.id.time_card);

        userName = findViewById(R.id.user_name);
        battery = findViewById(R.id.user_battery);
        brand = findViewById(R.id.user_brand);
        mileage = findViewById(R.id.user_mileage);
        chargingTime = findViewById(R.id.user_charging_time);
        balance = findViewById(R.id.user_balance);
        appointmentName = findViewById(R.id.user_appointment_name);
        appointmentTime = findViewById(R.id.user_appointment_time);
        appointmentQueue = findViewById(R.id.user_appointment_queue);
        recordName = findViewById(R.id.user_record_name);
        recordMoney = findViewById(R.id.user_record_money);
        recordTime = findViewById(R.id.user_record_time);
        collectionCount = findViewById(R.id.user_collection_count);
        collectionName = findViewById(R.id.user_collection_name);
        closeName = findViewById(R.id.user_close_name);
        closeTime = findViewById(R.id.user_close_time);
        closeDistance = findViewById(R.id.user_close_distance);
        timeName = findViewById(R.id.user_time_name);
        timeTime = findViewById(R.id.user_time_time);
        timeDistance = findViewById(R.id.user_time_distance);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//将活动移除活动收集器
    }
}
