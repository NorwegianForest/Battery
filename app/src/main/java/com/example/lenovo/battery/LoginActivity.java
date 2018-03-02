package com.example.lenovo.battery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button button1;  //登录按钮
    private Button button2;  //注册按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button1 = (Button) findViewById(R.id.login);
        final Activity activity = this;
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,UserActivity.class);
                startActivity(intent);
                /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                AlertDialog show = builder.setMessage("确定取消预约")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "预约已取消", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .show();*/
            }
        });//登录按钮的点击事件
        button2 = (Button) findViewById(R.id.register);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });//注册按钮的点击事件
    }
}
