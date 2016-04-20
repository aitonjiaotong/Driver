package com.aiton.bamin.driver.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiton.bamin.driver.R;
import com.aiton.bamin.driver.fragment.HuoDongFragment01;
import com.aiton.bamin.driver.fragment.HuoDongFragment02;

public class JiangLiHuoDongActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mHuoDong_viewpager;
    private TextView mTextView_huoDongIng;
    private TextView mTextView_huoDongEd;
    private ImageView mImageView_huoDongState;
    private int mAiton_basic_color;
    private int mText_gray_color;
    private int offset;
    private int currentPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiang_li_huo_dong);
        findID();
        getRes();
        initUI();
        setListener();
    }

    private void getRes() {
        mAiton_basic_color = getResources().getColor(R.color.aiton_basic_color);
        mText_gray_color = getResources().getColor(R.color.text_gray);
    }

    private void findID() {
        mTextView_huoDongIng = (TextView) findViewById(R.id.textView_HuoDongIng);
        mTextView_huoDongEd = (TextView) findViewById(R.id.textView_HuoDongEd);
        mImageView_huoDongState = (ImageView) findViewById(R.id.imageView_HuoDongState);
        mHuoDong_viewpager = (ViewPager) findViewById(R.id.huoDong_Viewpager);
    }

    private void initUI() {
        initAnim();
        mHuoDong_viewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mHuoDong_viewpager.addOnPageChangeListener(new MyPageChangeListener());
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position==0){
                setTab01(1);
            }else if(position==1){
                setTab02(2);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initAnim() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        Log.e("screenW", "InitImageView " + screenW);
        offset = screenW / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        mImageView_huoDongState.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void setListener() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        mTextView_huoDongIng.setOnClickListener(this);
        mTextView_huoDongEd.setOnClickListener(this);
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                HuoDongFragment01 huoDongFragment01 = new HuoDongFragment01();
                return huoDongFragment01;
            } else if (position == 1) {
                HuoDongFragment02 huoDongFragment02 = new HuoDongFragment02();
                return huoDongFragment02;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_HuoDongIng:
                setTab01(1);
                break;
            case R.id.textView_HuoDongEd:
                setTab02(2);
                break;
            case R.id.imageView_back:
                finish();
                break;
        }
    }

    private void setTab01(int position) {
        if (position != currentPosition) {
            currentPosition = position;
            Animation animation = new TranslateAnimation(offset, 0, 0, 0);
            mTextView_huoDongIng.setTextColor(mAiton_basic_color);
            mTextView_huoDongEd.setTextColor(mText_gray_color);
            mHuoDong_viewpager.setCurrentItem(0);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            mImageView_huoDongState.startAnimation(animation);
        }
    }

    private void setTab02(int position) {
        if (position != currentPosition) {
            currentPosition = position;
            Animation animation = new TranslateAnimation(0, offset, 0, 0);
            mTextView_huoDongIng.setTextColor(mText_gray_color);
            mTextView_huoDongEd.setTextColor(mAiton_basic_color);
            mHuoDong_viewpager.setCurrentItem(1);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            mImageView_huoDongState.startAnimation(animation);
        }
    }
}
