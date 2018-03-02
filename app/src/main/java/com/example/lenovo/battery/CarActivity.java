package com.example.lenovo.battery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CarActivity extends AppCompatActivity {

    private List<Car> mList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button button;   //按钮：添加爱车

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        toolbar = (Toolbar) findViewById(R.id.toolbarcar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initItem();  //汽车信息的初始化
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CarAdapter adapter = new CarAdapter(mList);
        recyclerView.setAdapter(adapter);

        button = (Button) findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarActivity.this,AddCarActivity.class);
                startActivity(intent);

            }
        });

    }

    private void initItem(){
        Car one = new Car("车1","管理权限",0.5);
        mList.add(one);
        Car two = new Car("车2","普通权限",0.6);
        mList.add(two);
        Car three = new Car("车3","普通权限",0.07);
        mList.add(three);
    }
}
