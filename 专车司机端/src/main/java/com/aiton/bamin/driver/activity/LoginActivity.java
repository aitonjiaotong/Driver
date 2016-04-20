package com.aiton.bamin.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.aiton.bamin.driver.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initID();
        initUI();
        setListener();
    }

    private void initID() {

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
        switch (v.getId()){
            case R.id.textView_forgetPassword:
                intent.setClass(LoginActivity.this,BackPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    /**
     * 双击退出应用
     */
    private long currentTime = 0;
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-currentTime>1000){
                Toast toast = Toast.makeText(LoginActivity.this, "双击退出应用", Toast.LENGTH_SHORT);
                toast.show();
                currentTime=System.currentTimeMillis();
                return false;
            }else{
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    };
}
