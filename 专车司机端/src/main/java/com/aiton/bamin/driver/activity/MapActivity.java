package com.aiton.bamin.driver.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aiton.bamin.driver.R;
import com.aiton.bamin.driver.listener.MyOrientationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //定位相关
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongitude;
    private String addressStr;
    //自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private int mXDirection;
    private float mCurrentAccracy;
    private AlertDialog mAlertDialog;
    private RelativeLayout mRela_onTheRoad;
    private LinearLayout mLinear_arrive;
    private Button mButton_getPassager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findID();
        initUI();
        initOritationListener();
        initLocation();
        setListener();
    }

    private void findID() {
        mRela_onTheRoad = (RelativeLayout) findViewById(R.id.rela_onTheRoad);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mLinear_arrive = (LinearLayout) findViewById(R.id.linear_arrive);
        mButton_getPassager = (Button) findViewById(R.id.button_getPassager);
    }

    private void initUI() {
        initMap();
    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        //构造一个更新地图的msu对象，然后设置该对象为缩放等级(比例尺)，最后设置地图状态。
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
    }

    private void setListener() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.imageView_call).setOnClickListener(this);
        mButton_getPassager.setOnClickListener(this);
        mLinear_arrive.setOnClickListener(this);
    }


    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //初始化定位的图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.ico_location_big_highlight_map);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_call:
                //用intent启动拨打电话
                Intent intent01 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "15871105320"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                }else{
                    startActivity(intent01);
                }
                break;
            case R.id.button_cancle:
                mAlertDialog.cancel();
                break;
            case R.id.button_ok:
                mAlertDialog.cancel();
                mRela_onTheRoad.setVisibility(View.VISIBLE);
                mButton_getPassager.setVisibility(View.GONE);
                mLinear_arrive.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_arrive:
                intent.setClass(MapActivity.this,CommitOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.button_getPassager:
                getPassagerDialog();
                break;
            case R.id.imageView_back:
                finish();
                break;
        }
    }

    private void getPassagerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        View inflate = getLayoutInflater().inflate(R.layout.getpassager_dialog, null);
        builder.setView(inflate);
        inflate.findViewById(R.id.button_cancle).setOnClickListener(this);
        inflate.findViewById(R.id.button_ok).setOnClickListener(this);
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开启定位
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        // 开启方向传感器
        myOrientationListener.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //停止定位
        mLocationClient.stop();
        // 关闭方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
//    定位的监听回调

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 构造定位数据
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())//获得半径
                    .direction(mXDirection)
                    .latitude(bdLocation.getLatitude())//获得经度
                    .longitude(bdLocation.getLongitude())//获得纬度
                    .build();
            mCurrentAccracy = bdLocation.getRadius();
            //设置定位数据
            mBaiduMap.setMyLocationData(data);
            //设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);

            //初始化经纬度
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();

            //第一次进入，定位到所在位置
            if (isFirstIn) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
                addressStr = bdLocation.getAddrStr();

                LatLng llText = new LatLng(mLatitude, mLongitude);
                //构建文字Option对象，用于在地图上添加文字
                OverlayOptions textOption = new TextOptions()
                        .fontSize(24)
                        .fontColor(0xFF000000)
                        .text(addressStr)
                        .position(llText);
                //在地图上添加该文字对象并显示
                mBaiduMap.addOverlay(textOption);

            }
        }
    }

    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;

                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mCurrentAccracy)
                                        // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mXDirection)
                                .latitude(mLatitude)
                                .longitude(mLongitude).build();
                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                .fromResource(R.mipmap.ico_location_big_highlight_map);
                        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, mCurrentMarker);
                        mBaiduMap.setMyLocationConfigeration(config);
                    }
                });
    }
}
