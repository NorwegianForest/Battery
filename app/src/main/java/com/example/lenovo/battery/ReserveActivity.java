package com.example.lenovo.battery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReserveActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView address;//预约电站地址
    private TextView name;  //预约电站名字
    private TextView result;//预约结果
    private TextView time;  //预约时间
    private TextView one;   //其他信息
    private TextView two;   //其他信息
    private Button button1;//收藏按钮
    private Button button2;//取消预约按钮
    private Button button3;//订单完成按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        toolbar=(Toolbar) findViewById(R.id.toolbarreserve);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Activity activity=this;
        button1 = (Button) findViewById(R.id.collect);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("确定收藏该电站？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .show();
            }
        });  //收藏按钮的点击事件

        button2 = (Button) findViewById(R.id.reserve_delete);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                AlertDialog show = builder.setMessage("确定取消预约")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "预约已取消", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .show();
            }
        });   //取消订单按钮的点击事件

        button3 = (Button) findViewById(R.id.reserve_finish);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("确定订单完成，进行支付？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "订单完成", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .show();
            }
        });   //订单完成按钮的点击事件

        //预约信息设置
        address = (TextView) findViewById(R.id.address);
        address.setText("西南交通大学");

        name = (TextView) findViewById(R.id.name);
        name.setText("某电站");

        time = (TextView) findViewById(R.id.time);
        time.setText("17：00");

        result = (TextView) findViewById(R.id.result);
        result.setText("预约电池/预约充电桩");

        one = (TextView) findViewById(R.id.c);
        one.setText("30");

        two = (TextView) findViewById(R.id.d);
        two.setText("60");
    }
}
