package com.example.lenovo.battery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.battery.Constants;
import com.battery.Database;
import com.battery.HttpUtil;
import com.battery.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button loginButton;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.et_userName);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);

        handler = new Handler();
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userName.getText())) {
                    Toast.makeText(view.getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(view.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }
            }
        });
    }

    private void login() {
        RequestBody body = new FormBody.Builder()
                .add("phone", userName.getText().toString())
                .add("password", password.getText().toString()).build();
        HttpUtil.sendRequest(Constants.LOGINADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("illegal")) {
                    Snackbar.make(findViewById(R.id.activity_login), "登录失败", Snackbar.LENGTH_SHORT).show();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(runLogin);
                        }
                    }.start();
                }
            }
        });
    }

    Runnable runLogin = new Runnable() {
        @Override
        public void run() {
            Database.setNoDefault();
            Database.setNewDefault(userName.getText().toString(), password.getText().toString(), 1);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            ActivityCollector.finishAll();
        }
    };
}
