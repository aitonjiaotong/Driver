package com.aiton.bamin.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aiton.bamin.driver.R;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
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
        findViewById(R.id.rela_mine).setOnClickListener(this);
        findViewById(R.id.relativeLayout_jine).setOnClickListener(this);
        findViewById(R.id.rela_chaxun).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.rela_mine:
                intent.setClass(MineActivity.this, MineInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.relativeLayout_jine:
                intent.setClass(MineActivity.this, MyMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_chaxun:
                intent.setClass(MineActivity.this, ChaXunActivity.class);
                startActivity(intent);
                break;
        }
    }
}
