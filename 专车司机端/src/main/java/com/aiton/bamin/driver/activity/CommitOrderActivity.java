package com.aiton.bamin.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aiton.bamin.driver.R;

public class CommitOrderActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_order);
        findID();
        initUI();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.button_faqishoukuan).setOnClickListener(this);
    }

    private void findID() {

    }

    private void initUI() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.button_faqishoukuan:
                intent.setClass(CommitOrderActivity.this, CommitOrderNextActivity.class);
                startActivity(intent);
                break;
        }
    }
}
