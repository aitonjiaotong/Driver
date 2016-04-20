package com.aiton.bamin.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aiton.bamin.driver.R;

public class ChaXunActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_xun);
        findID();
        initUI();
        setListener();
    }

    private void findID() {

    }

    private void initUI() {

    }

    private void setListener() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.rela_chengjiaolv).setOnClickListener(this);
        findViewById(R.id.rela_fencheng).setOnClickListener(this);
        findViewById(R.id.rela_JiangLiHuoDong).setOnClickListener(this);
        findViewById(R.id.rela_chengjichaxun).setOnClickListener(this);
        findViewById(R.id.rela_FuWuPingJia).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.rela_chengjiaolv:
                intent.setClass(ChaXunActivity.this, ChengJiaoLvActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_fencheng:
                intent.setClass(ChaXunActivity.this, FenChengActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_JiangLiHuoDong:
                intent.setClass(ChaXunActivity.this, JiangLiHuoDongActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_chengjichaxun:
                intent.setClass(ChaXunActivity.this, ChengJiChaXunActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_FuWuPingJia:
                intent.setClass(ChaXunActivity.this, FuWuPingFenActivity.class);
                startActivity(intent);
                break;
        }
    }
}
