package com.example.lenovo.battery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class BalanceActivity extends AppCompatActivity {

    TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        MainActivity.setStatusBarColor(this);

        Toolbar toolbar = findViewById(R.id.balance_toolbar);
        toolbar.setTitle("余额");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        balance = findViewById(R.id.balance_balance);
        balance.setText(getIntent().getStringExtra("balance"));
    }
}
