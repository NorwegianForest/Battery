package com.example.lenovo.battery;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.Record;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Record> recordList;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        MainActivity.setStatusBarColor(this);

        Toolbar toolbar = findViewById(R.id.record_toolbar);
        toolbar.setTitle("换电记录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        handler = new Handler();
        recyclerView = findViewById(R.id.record_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String id = getIntent().getStringExtra("id");
        loadRecord(id);
    }

    private void loadRecord(String id) {
        RequestBody body = new FormBody.Builder().add("user_id", id).build();
        HttpUtil.sendRequest(Constants.RECORDADDRESS,  body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {
                    Log.d("RecordActivity", "换电数据无结果");
                } else {
                    recordList = new Gson().fromJson(responseData, new TypeToken<List<Record>>() {}.getType());
                    for (Record record : recordList) {
                        record.loadStation();
                        record.loadOldBattery();
                        record.loadNewBattery();
                    }
                    new Thread(){
                        public void run(){
                            handler.post(setListRunnable);
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

    Runnable setListRunnable = new  Runnable(){
        @Override
        public void run() {
            //更新界面
            RecordAdapter adapter = new RecordAdapter(recordList);
            recyclerView.setAdapter(adapter);
        }
    };
}
