package com.example.lenovo.battery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * 若用户当前没有预约，点击导航菜单中的预约选项后，即转跳到此活动
 */

public class NoAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_appointment);
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
    }
}
