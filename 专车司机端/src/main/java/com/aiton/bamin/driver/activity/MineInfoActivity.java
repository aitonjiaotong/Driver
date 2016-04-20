package com.aiton.bamin.driver.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aiton.bamin.driver.R;

public class MineInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_back:
                finish();
                break;
        }
    }
}
