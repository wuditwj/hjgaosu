package com.gaosu.heijing;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaosu.heijing.base.BaseActivity;

import com.gaosu.heijing.broadcast.ScreenReciver;
import com.gaosu.heijing.constant.Constant;
import com.gaosu.heijing.constant.Url;
import com.gaosu.heijing.entity.UpdateEntity;
import com.gaosu.heijing.service.DownMOLIVideoService;
import com.gaosu.heijing.util.AppUtil;
import com.gaosu.heijing.util.ErweimaUtil;
import com.gaosu.heijing.util.HwUtil;
import com.gaosu.heijing.util.TimeUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;

public class MainActivity2 extends BaseActivity implements ScreenReciver.Callback {
    boolean canfd = true;
    boolean cansx = true;

    @Override
    public void kaojin() {
        if (cansx) {
            sx();
        }

    }

    @Override
    public void yuanli() {
        if (canfd) {


            fd();
        }

    }


    //    private void fd() {
//        canfd = false;
//        HwUtil.TURNON();
//
//        ViewCompat.animate(relativeLayout)
//                .setDuration(200)
//                .scaleY(1f)
//                .scaleX(1f)
//                .setListener(new ViewPropertyAnimatorListener() {
//                    @Override
//                    public void onAnimationStart(View view) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(View view) {
//                        canfd = true;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(View view) {
//
//                    }
//                })
//                .start();
//
//    }
//
//    private void sx() {
//        cansx = false;
//
//        ViewCompat.animate(relativeLayout)
//                .setDuration(200)
//                .scaleY(0.01f)
//                .scaleX(0.01f)
//                .setListener(new ViewPropertyAnimatorListener() {
//                    @Override
//                    public void onAnimationStart(View view) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(View view) {
//                        cansx = true;
//                        HwUtil.TURNOFF();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(View view) {
//
//                    }
//                })
//
//
//                .start();
//
//    }
    private void fd() {
        canfd = false;
        HwUtil.TURNON();

        ViewCompat.animate(relativeLayout)
                .setDuration(300)
                .alpha(1)

                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        canfd = true;
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();

    }

    private void sx() {
        cansx = false;

        ViewCompat.animate(relativeLayout)
                .setDuration(200)
                .alpha(0)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        cansx = true;
                        HwUtil.TURNOFF();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })


                .start();

    }

    int versionCode = 0;
    ImageView iv_ewm;
    TextView tv_ewm;
    TextView tv_banben;
    TextView tv_version;
    TextView tv_code;
    LinearLayout ll_info;
    ImageView iv_adv;
    RelativeLayout relativeLayout;
    TextView tv_test;

    int INTERTIME = 1000 * 60 * 5;
    int ADVTIME = 15000;
    int[] advList = {R.raw.a2,R.raw.a1};
    private int index = 0;
    @SuppressLint("HandlerLeak")
    public Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateHandler.sendEmptyMessageDelayed(1, INTERTIME);
            Log.i("aaa", "检查更新");
            checkUpdate();
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler advHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            index++;
            if (index >= advList.length) {
                index = 0;
            }
            iv_adv.setImageResource(advList[index]);
            advHandler.sendEmptyMessageDelayed(1, ADVTIME);
        }
    };
    @SuppressLint("HandlerLeak")
    public static Handler screenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TimeUtil.isInDate(new Date(), "06:00:00", "21:00:00")) {
                if ("0".equals(HwUtil.GETST())) {
                    HwUtil.TURNON();
                }

            } else {
                HwUtil.TURNOFF();
            }

            screenHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 5);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.NavigationBarStatusBar(this, true);
        setContentView(R.layout.activity_main2);

        initView();
        initClick();
        // hideBar();

        //startRemind();
        registSreccnReceiver();
        iv_adv.setImageResource(advList[0]);
        updateHandler.sendEmptyMessageDelayed(1, INTERTIME);
        advHandler.sendEmptyMessageDelayed(1, ADVTIME);
        screenHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 5);

    }


    private void initClick() {
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();


            }
        });
        ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_info.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initView() {
        relativeLayout = findViewById(R.id.rl);
        iv_ewm = findViewById(R.id.iv_ewm);
        iv_adv = findViewById(R.id.iv_ad);
        tv_ewm = findViewById(R.id.tv_ewm);
        tv_banben = findViewById(R.id.tv_banben);
        tv_version = findViewById(R.id.tv_version);
        ll_info = findViewById(R.id.ll_info);
        tv_code = findViewById(R.id.tv_code);
        tv_test=findViewById(R.id.tv_test);
        try {
            versionCode = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionCode;
            tv_code.setText("软件版本" + versionCode);
            tv_banben.setText("软件版本" + versionCode);
            tv_test.setText("软件版本" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tv_ewm.setText(Constant.MAC_ADDRESS);
        iv_ewm.setImageBitmap(ErweimaUtil.createQRcodeImage(Constant.MAC_ADDRESS));
    }


    @Override
    protected void onResume() {


        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacksAndMessages(null);
        advHandler.removeCallbacksAndMessages(null);
        screenHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(screenReciver);
    }

    void checkUpdate() {
        Map<String, String> map = new HashMap<>();
        // map.put("type", "4");
        OkHttpUtils.get().url(Url.UPDATE_VERSION).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MainActivity2.this, "网络异常...", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResponse(String response, int id) {

                Log.i("aaa", response);

                Gson gson = new Gson();
                UpdateEntity updateEntity = gson.fromJson(response, UpdateEntity.class);
                Log.i("aaa", updateEntity.getDownload());

                if (versionCode < updateEntity.getVersion()) {
                    Toast.makeText(MainActivity2.this, "检测到新版本，开始更新...", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity2.this, DownMOLIVideoService.class);
                    intent.putExtra("url", updateEntity.getDownload());
                    intent.putExtra("versionCode", versionCode);
                    intent.putExtra("size", updateEntity.getFile_size());
                    startService(intent);
                } else {
                    Toast.makeText(MainActivity2.this, "当前是最新版本", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == 4) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(MainActivity2.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity2.this);
        inputDialog.setTitle("请输入密码").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mima = editText.getText().toString();
                        if (mima != null && mima.equals("1")) {
                            showListDialog();
                        } else if (mima != null && mima.equals("2")) {

                            sx();
                        } else if (mima != null && mima.equals("3")) {
                            fd();
                        }

                    }
                }).show();
    }

    //
    private void showListDialog() {
        final String[] items = {"设置WIFI", "显示mac地址", "检查更新", "启动系统页"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(MainActivity2.this);
        listDialog.setTitle("选项");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        break;
                    case 1:
                        ll_info.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        checkUpdate();
                        break;
                    case 3:
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ComponentName comp = new ComponentName("com.android.launcher3", "com.android.launcher3.Launcher");
                        intent.setComponent(comp);
                        intent.setAction("android.intent.action.View");
                        startActivity(intent);
                        break;

                    default:
                        break;

                }
            }
        });
        listDialog.show();
    }


    //


    //注册广播
    public ScreenReciver screenReciver;

    void registSreccnReceiver() {
        //
        screenReciver = new ScreenReciver();
        screenReciver.setListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HOOK_EVENT");
        registerReceiver(screenReciver, intentFilter);
    }


}
