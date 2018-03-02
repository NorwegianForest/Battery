package com.example.lenovo.battery;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.Station;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectionActivity extends AppCompatActivity {

    private List<com.battery.Station> stationList;
    private RecyclerView recyclerView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        MainActivity.setStatusBarColor(this);

        Toolbar toolbar = findViewById(R.id.collection_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("收藏电站");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String id = getIntent().getStringExtra("id");

        recyclerView = findViewById(R.id.collection_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        handler = new Handler();
        loadStationList(id);
    }

    private void loadStationList(String id) {
        RequestBody body = new FormBody.Builder().add("user_id", id).build();
        HttpUtil.sendRequest(Constants.COLLECTIONADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                stationList = new Gson().fromJson(responseData,
                        new TypeToken<List<Station>>(){}.getType());
                new Thread(){
                    public void run(){
                        handler.post(setListRunnable);
                    }
                }.start();
            }
        });
    }

    Runnable setListRunnable = new  Runnable(){
        @Override
        public void run() {
            //更新界面
            StationAdapterPro adapter = new StationAdapterPro(stationList);
            int userId = Integer.parseInt(getIntent().getStringExtra("id"));
            adapter.setUserId(userId);
            recyclerView.setAdapter(adapter);
        }
    };
}
