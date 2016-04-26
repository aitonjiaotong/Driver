package com.aiton.bamin.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aiton.bamin.driver.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditText_phone;
    private EditText mEditText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initID();
        initUI();
        setListener();
    }

    private void initID() {
        mEditText_phone = (EditText) findViewById(R.id.editText_phone);
        mEditText_password = (EditText) findViewById(R.id.editText_password);
    }

    private void initUI() {

    }

    private void setListener() {
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.textView_forgetPassword).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.textView_forgetPassword:
                intent.setClass(LoginActivity.this, BackPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                String phoneNum = mEditText_phone.getText().toString().trim();
                String password = mEditText_password.getText().toString().trim();
                if ("8000".equals(phoneNum) && "111111".equals(password)) {
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Log.e("onClick", "不闹哪样");
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("onClick", "闹哪样");
                    Toast.makeText(LoginActivity.this, "您输入的账号密码有误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 双击退出应用
     */
    private long currentTime = 0;

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentTime > 1000) {
                Toast toast = Toast.makeText(LoginActivity.this, "双击退出应用", Toast.LENGTH_SHORT);
                toast.show();
                currentTime = System.currentTimeMillis();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    ;
}
