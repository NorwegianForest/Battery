package com.example.lenovo.battery;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddCarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button button;//按钮：添加汽车


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        toolbar = (Toolbar) findViewById(R.id.toolbaradd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Activity activity = this;
        button = (Button) findViewById(R.id.addcar);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "添加成功", Toast.LENGTH_SHORT).show();
            }
        });//按钮添加汽车的点击事件
    }

}
