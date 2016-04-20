package com.aiton.bamin.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aiton.bamin.driver.R;

public class MyMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView_shouzhimingxi;
    private TextView mTextView_tixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);
        findID();
        initUI();
        setListener();
    }

    private void findID() {
        mListView_shouzhimingxi = (ListView) findViewById(R.id.listView_shouzhimingxi);
        mTextView_tixian = (TextView) findViewById(R.id.textView_tixian);
    }

    private void initUI() {
        View shouzhimingxi_foot = getLayoutInflater().inflate(R.layout.shouzhimingxi_foot, null);
        mListView_shouzhimingxi.addFooterView(shouzhimingxi_foot);
        mListView_shouzhimingxi.setAdapter(new MyAdapter());
    }

    private void setListener() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        mTextView_tixian.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.textView_tixian:
                intent.setClass(MyMoneyActivity.this,TiXianActivity.class);
                startActivity(intent);
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.shouzhimingxi_item, null);
            return inflate;
        }
    }
}
