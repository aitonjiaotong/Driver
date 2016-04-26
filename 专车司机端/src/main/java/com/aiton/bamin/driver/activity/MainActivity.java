package com.aiton.bamin.driver.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.upgrade.UpgradeUtils;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.HTTPUtils;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;
import com.aiton.bamin.driver.R;
import com.aiton.bamin.driver.contant.Contant;
import com.aiton.bamin.driver.contant.VersionAndHouTaiIsCanUse;
import com.aiton.bamin.driver.util.ScreenUtils;
import com.android.volley.VolleyError;
import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.navisdk.BNaviEngineManager;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.comapi.tts.BNTTSPlayer;
import com.baidu.navisdk.comapi.tts.BNavigatorTTSPlayer;
import com.baidu.navisdk.comapi.tts.IBNTTSPlayerListener;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import Decoder.BASE64Decoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView_shoufache;
    private int mColor_white;
    private Drawable mDrawable_circle_gray;
    private boolean isShouFaChe = false;
    private Drawable mDrawable_circle_white;
    private int mColor_aiton_basic_color;
    private ImageView mImageView_lianjie;
    private RelativeLayout mRela_lianjie;
    private int[] mLocation;
    private int mScreenHeight;
    private PopupWindow mPopupWindow;
    private String mId;
    private String mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSp();
        //检查服务器是否存活和当前版本是否可用
        checkVersionAndHouTaiIsCanUse();
        getResoure();
        initDaoHang();
        findID();
        initUI();
        setListener();
    }

    private void initDaoHang() {
        //初始化导航引擎
        BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
                mNaviEngineInitListener, new LBSAuthManagerListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        String str = null;
                        if (0 == status) {
                            str = "key校验成功!";
                        } else {
                            str = "key校验失败, " + msg;
                        }
                        Toast.makeText(MainActivity.this, str,
                                Toast.LENGTH_LONG).show();
                        Log.e("onAuthResult", "str" + str);
                    }
                });
    }

    private BNaviEngineManager.NaviEngineInitListener mNaviEngineInitListener = new BNaviEngineManager.NaviEngineInitListener() {
        public void engineInitSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "百度导航初始化成功", Toast.LENGTH_SHORT).show();

                }
            });
            Log.e("engineInitSuccess", "百度导航初始化成功");
        }

        public void engineInitStart() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "百度导航初始化开始", Toast.LENGTH_SHORT).show();

                }
            });
            Log.e("engineInitSuccess", "百度导航初始化开始");
        }

        public void engineInitFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "百度导航初始化失败", Toast.LENGTH_SHORT).show();

                }
            });
            Log.e("engineInitSuccess", "百度导航初始化失败");
        }
    };

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void initSp() {
        SharedPreferences sp = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        mId = sp.getString("id", "");
        mDeviceId = sp.getString("DeviceId", "");
    }

    private void checkVersionAndHouTaiIsCanUse() {
        String url = Contant.HOST + "/bmpw/check/live";
        Map<String, String> map = new HashMap<>();
        map.put("flag", "11");
        HTTPUtils.post(MainActivity.this, url, map, new VolleyListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setDialogCkeck("服务器正在升级，暂停服务", "确认");
            }

            @Override
            public void onResponse(String s) {
                VersionAndHouTaiIsCanUse versionAndHouTaiIsCanUse = GsonUtils.parseJSON(s, VersionAndHouTaiIsCanUse.class);
                int ableVersion = versionAndHouTaiIsCanUse.getAbleVersion();
                if (versionAndHouTaiIsCanUse.isLive()) {
                    if (Contant.ABLEVERSION < ableVersion) {
                        setDialogCkeck("当前版本不可用，请去应用商店下载最新版本", "确认");
                    } else {
                        checkUpGrade();
                        //        检查是否在其他设备上登陆
                        checkIsLoginOnOtherDevice();
                    }
                } else {
                    setDialogCkeck(versionAndHouTaiIsCanUse.getMessage(), "确认");
                }
            }
        });
    }

    /**
     * 检查是否在其他设备上登陆
     */
    private void checkIsLoginOnOtherDevice() {
        if (!"".equals(mDeviceId)) {
            String url = Contant.HOST + "/bmpw/account/findLogin_id";
            Map<String, String> map = new HashMap<>();
            map.put("account_id", mId);
            map.put("flag", "11");
            HTTPUtils.post(MainActivity.this, url, map, new VolleyListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }

                @Override
                public void onResponse(String s) {
                    if (!s.equals(mDeviceId)) {
                        setDialog("你的账号登录异常\n请重新登录", "确定");
                    }
                }
            });
        }
    }

    /**
     * 弹出未登录按钮跳转登录界面并清除登录信息
     *
     * @param messageTxt
     * @param iSeeTxt
     */
    private void setDialog(String messageTxt, String iSeeTxt) {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        TextView message = (TextView) commit_dialog.findViewById(R.id.message);
        Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
        message.setText(messageTxt);
        ISee.setText(iSeeTxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.setView(commit_dialog)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //清除用户登录信息
                SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear();
                edit.commit();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkUpGrade() {
        UpgradeUtils.checkUpgrade(MainActivity.this, Contant.URL.UP_GRADE);
    }

    private void getHeight() {
        mLocation = new int[2];
        mRela_lianjie.getLocationOnScreen(mLocation);
        mScreenHeight = ScreenUtils.getScreenHeight(MainActivity.this);
    }

    private void setListener() {
        mTextView_shoufache.setOnClickListener(this);
        findViewById(R.id.textView_more).setOnClickListener(this);
        findViewById(R.id.textView_mine).setOnClickListener(this);
    }

    private void initUI() {
        Animation animation_rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        if (animation_rotate != null) {
            mImageView_lianjie.startAnimation(animation_rotate);
        }
    }

    private void getResoure() {
        mDrawable_circle_gray = getResources().getDrawable(R.drawable.circle_gray_transparent);
        mDrawable_circle_white = getResources().getDrawable(R.drawable.circle_white);
        mColor_white = getResources().getColor(R.color.white);
        mColor_aiton_basic_color = getResources().getColor(R.color.aiton_basic_color);
    }

    private void findID() {
        mTextView_shoufache = (TextView) findViewById(R.id.textView_shoufache);
        mImageView_lianjie = (ImageView) findViewById(R.id.imageView_lianjie);
        mRela_lianjie = (RelativeLayout) findViewById(R.id.rela_lianjie);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.textView_more:
                intent.setClass(MainActivity.this, MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.textView_mine:
                intent.setClass(MainActivity.this, MineActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_startMap:

                intent.setClass(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.textView_canclePopup:
                mPopupWindow.dismiss();
                break;
            case R.id.textView_shoufache:
                isShouFaChe = !isShouFaChe;
                if (isShouFaChe) {
//                    outVoice("开始接单了");
                    // 初始化TTS. 开发者也可以使用独立TTS模块，不用使用导航SDK提供的TTS
                    BNTTSPlayer.initPlayer();
                    BNTTSPlayer.playTTSText("开始接单了！", 1);
                    //设置TTS播放回调
                    BNavigatorTTSPlayer.setTTSPlayerListener(new IBNTTSPlayerListener() {

                        @Override
                        public int playTTSText(String arg0, int arg1) {
                            //开发者可以使用其他TTS的API

                            return BNTTSPlayer.playTTSText("开始接单了", arg1);
                        }

                        @Override
                        public void phoneHangUp() {
                            //手机挂断
                        }

                        @Override
                        public void phoneCalling() {
                            //通话中
                        }

                        @Override
                        public int getTTSState() {
                            //开发者可以使用其他TTS的API,
                            return BNTTSPlayer.getTTSState();
                        }
                    });
                    mTextView_shoufache.setBackground(mDrawable_circle_gray);
                    mTextView_shoufache.setTextColor(mColor_white);
                    mTextView_shoufache.setText("收车");
                    mRela_lianjie.setVisibility(View.VISIBLE);
                    moNiJieDan();
                } else {
                    mTextView_shoufache.setBackground(mDrawable_circle_white);
                    mTextView_shoufache.setTextColor(mColor_aiton_basic_color);
                    mTextView_shoufache.setText("发车");
                    mRela_lianjie.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
//    private void outVoice(String msg) {
//        Parameters para = new Parameters();
//        para.put("text",msg );
//        para.put("ctp", "1");
//        para.put("per", "0");
//        ApiStoreSDK.execute("http://apis.baidu.com/apistore/baidutts/tts", ApiStoreSDK.GET, para, new ApiCallBack() {
//            @Override
//            public void onSuccess(int status, String responseString) {
//                Log.e("onSuccess ", "onSuccess " + responseString);
//                System.out.println(responseString);
//                Voice voice = GsonUtils.parseJSON(responseString, Voice.class);
//                String retMsg = voice.getRetData();
//                try {
//                    String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    File file = new File(absolutePath + "/voice");
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    String s = file + "/voice.mp3";
//                    decoderBase64File(retMsg, s);
//                    File file1 = new File(s);
//                    Uri uri = Uri.fromFile(file1);
//                    MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                    if (mediaPlayer != null) {
//                        mediaPlayer.start();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e("onComplete ", "onComplete ");
//            }
//
//            @Override
//            public void onError(int status, String responseString, Exception e) {
//                Log.e("onError ", "onError " + responseString);
//            }
//
//        });
//    }

    /**
     * <p>将base64字符解码保存文件</p>
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    private void moNiJieDan() {
        //模拟接单
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isShouFaChe) {
//                                startCalcRoute();
                                setpopupWindows();
//                                outVoice("联谊大厦到思明区莲岳路松柏大厦对面，距离您还有0.3公里，点击进入地图");
                                mRela_lianjie.setBackgroundResource(R.mipmap.zhipai2x);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setpopupWindows() {
        View inflate = getLayoutInflater().inflate(R.layout.popupwindow_zhipai, null);
        inflate.findViewById(R.id.textView_canclePopup).setOnClickListener(this);
        inflate.findViewById(R.id.rela_startMap).setOnClickListener(this);
        //最后一个参数为true，点击PopupWindow消失,宽必须为match，不然肯呢个会导致布局显示不完全
        mPopupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置外部点击无效
        mPopupWindow.setOutsideTouchable(false);
        //设置背景变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        getHeight();
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        BitmapDrawable bitmapDrawable = new BitmapDrawable();
//        mPopupWindow.setBackgroundDrawable(bitmapDrawable);
        mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM, 0, mScreenHeight - mLocation[1] + 100);
    }

    /**
     * 双击退出应用
     */
    private long currentTime = 0;

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentTime > 1000) {
                Toast toast = Toast.makeText(MainActivity.this, "双击退出应用", Toast.LENGTH_SHORT);
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

    //dialog提示
    private void setDialogCkeck(String messageTxt, String iSeeTxt) {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        TextView message = (TextView) commit_dialog.findViewById(R.id.message);
        Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
        message.setText(messageTxt);
        ISee.setText(iSeeTxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.setView(commit_dialog)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
