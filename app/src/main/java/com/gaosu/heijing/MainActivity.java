package com.gaosu.heijing;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gaosu.heijing.base.BaseActivity;

import com.gaosu.heijing.base.BaseApplication;
import com.gaosu.heijing.broadcast.NetWorkChangReceiver;
import com.gaosu.heijing.broadcast.ScreenReciver;
import com.gaosu.heijing.constant.Constant;
import com.gaosu.heijing.constant.Url;
import com.gaosu.heijing.entity.ADVEntity;
import com.gaosu.heijing.entity.InitEntity;
import com.gaosu.heijing.entity.TopAdvEntity;
import com.gaosu.heijing.entity.UpdateEntity;
import com.gaosu.heijing.service.DownMOLIVideoService;
import com.gaosu.heijing.util.AdvSortUtil;
import com.gaosu.heijing.util.AppUtil;
import com.gaosu.heijing.util.ErweimaUtil;
import com.gaosu.heijing.util.FileUtil;
import com.gaosu.heijing.util.HwUtil;
import com.gaosu.heijing.util.MD5Util;
import com.gaosu.heijing.util.ScreenLightUtil;
import com.gaosu.heijing.util.SharedPreferencesUtil;
import com.gaosu.heijing.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import okhttp3.Call;


public class MainActivity extends BaseActivity implements ScreenReciver.Callback, NetWorkChangReceiver.WIFIInterface {

    boolean canON = true;
    boolean canOFF = true;
    static boolean canturnon = true;

    //static boolean isplay = false;
    int LOADADVTIME = 70000;
    //int LOADADVTIME = 3000;

    static boolean isKaojin = false;


    int versionCode = 0;
    ImageView iv_ewm;
    TextView tv_ewm;
    TextView tv_banben;
    TextView tv_version;
    TextView tv_code;
    LinearLayout ll_info;
    static ImageView iv_adv;
    static RelativeLayout relativeLayout;
    static RelativeLayout toprelativeLayout;
    static VideoView videoView;
    static VideoView topVideoView;
    RelativeLayout mask;
    int TOTALADV = 12;
    static ImageView iv_top;
    Button btkj;
    Button btyl;
    ImageView ivBanshou;
    //int INTERTIME = 1000 * 60 * 5;

    //int ADVTIME = 15000;
    //    int[] advList = {R.raw.a1, R.raw.a2, R.raw.a3
//    };

    int INTERTIME = 1000 * 60 * 60 * 2;
    static int ADVTIME = 15000;
    private static int index = 0;

    //注册广播
    public ScreenReciver screenReciver;
    private NetWorkChangReceiver netWorkChangReceiver;
    AlarmBroadcastReceiver alarmBroadcastReceiver;

    boolean cansettime = true;

    static int videotime = 0;

    static List<ADVEntity.DataBean> advList = new ArrayList<>();
    int time = 1;

    public static String FILEGAOSU = "agaosu";
    public static String FILEGAOSUB = "agaosub";

    static TextView tvtest;
    static TextView tvtestyj;

    private EditText et;
    private Button bt;

    //**********************************handler*****************************************************
    @SuppressLint("HandlerLeak")
    public Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateHandler.sendEmptyMessageDelayed(1, INTERTIME);
            AppUtil.NavigationBarStatusBar(MainActivity.this, true);
            Log.i("aaa", "检查更新");
            checkUpdate();


            init();
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler oncreatHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            init();
            checkUpdate();
//            if (cansettime){
//                cansettime=false;
//                setAlarm();
//
//            }


        }
    };

    @SuppressLint("HandlerLeak")
    public Handler heartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            OkHttpUtils.get().url(Url.BAIDU).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ivBanshou.setVisibility(View.VISIBLE);
                    ivBanshou.setBackgroundResource(R.drawable.xbsbaidu);
                    banshouDonghua();
                }

                @Override
                public void onResponse(String response, int id) {
                    ivBanshou.clearAnimation();
                    ivBanshou.setVisibility(View.GONE);
                    sendHeart();

                }
            });
            heartHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 10);

        }
    };

    @SuppressLint("HandlerLeak")
    public Handler checkScreenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
//                if (HwUtil.GETST().equals("0")) {
//                    if (isKaojin) {
//                        yuanli();
//                    }
//
//
//                }
//            }

            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
                if (HwUtil.GETST().equals("0")) {
                    if (isKaojin) {
                        yuanli();
                    }


                } else if (HwUtil.GETST().equals("1")) {
                    if (!isKaojin) {
                        kaojin();
                    }
                }
            }

            videotime++;
            checkScreenHandler.sendEmptyMessageDelayed(1, 1000);

        }
    };

    @SuppressLint("HandlerLeak")
    public static Handler advHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //
            //isplay = false;
            videotime = 0;
            if (index >= advList.size()) {
                index = 0;
            }
            if (advList.size() == 0) {
                return;
            }

            //显示a面
            if (!isKaojin) {
                if (advList.get(index).getType().equals("1")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
                                HwUtil.TURNON();
                            }

                        }
                    }, 100);
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .placeholder(iv_adv.getDrawable());
                    Glide.with(iv_adv.getContext()).load(advList.get(index).getMedia_url()).apply(requestOptions).into(iv_adv);
                    //延迟切换图片

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_adv.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.INVISIBLE);
                            videoView.stopPlayback();
                            topVideoView.setVisibility(View.INVISIBLE);
                            topVideoView.stopPlayback();

                        }
                    }, 500);

                    //index++;
                    advHandler.sendEmptyMessageDelayed(1, ADVTIME);
                } else if (advList.get(index).getType().equals("2")) {
                    //显示a面视频
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
                                HwUtil.TURNON();
                            }
                        }
                    }, 100);


                    String filename = FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + MD5Util.encrypt(advList.get(index).getMedia_url()) + ".mp4";
                    //index++;
                    boolean isExists = FileUtil.fileIsExists(filename);
                    if (isExists) {

//                        iv_adv.setVisibility(View.INVISIBLE);
//                        videoView.setVisibility(View.VISIBLE);
//                        topVideoView.setVisibility(View.INVISIBLE);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                    @Override
                                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                                            videoView.setBackgroundColor(Color.TRANSPARENT);

                                        return true;
                                    }
                                });
                            }
                        });

                        videoView.setVideoPath(filename);


                        videoView.start();
                        videoView.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                topVideoView.setVisibility(View.INVISIBLE);

                                iv_adv.setVisibility(View.INVISIBLE);

                            }
                        }, 400);

                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                //isplay = false;

                                // advHandler.sendEmptyMessage(1);
                            }
                        });
                        //播放异常
                        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                videoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                                //advHandler.sendEmptyMessage(1);
                                return true;
                            }
                        });

                        //index++;
                        advHandler.sendEmptyMessageDelayed(1, ADVTIME);
                    } else {

                        index++;
                        advHandler.sendEmptyMessageDelayed(1, ADVTIME);

                    }
                } else {
                    HwUtil.TURNOFF();
                    //index++;
                    advHandler.sendEmptyMessageDelayed(1, ADVTIME);
                }
            } else {
                if (advList.get(index).getId() != "-1" && advList.get(index).getType_b() != null && advList.get(index).getType_b().equals("1")
                        && advList.get(index).getMedia_url_b() != null && advList.get(index).getMedia_url_b() != "") {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            HwUtil.TURNON();
//                        }
//                    }, 100);
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .placeholder(iv_adv.getDrawable());
                    Glide.with(iv_adv.getContext()).load(advList.get(index).getMedia_url_b()).apply(requestOptions).into(iv_adv);
                    //延迟切换图片

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_adv.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.INVISIBLE);
                            videoView.stopPlayback();
                            topVideoView.setVisibility(View.INVISIBLE);
                            topVideoView.stopPlayback();
                            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
                                HwUtil.TURNON();
                            }

                        }
                    }, 700);


                    //
                    //index++;

                    advHandler.sendEmptyMessageDelayed(1, ADVTIME);
                } else if (advList.get(index).getId() != "-1" && advList.get(index).getType_b() != null && advList.get(index).getType_b().equals("2")
                        && advList.get(index).getMedia_url_b() != null && advList.get(index).getMedia_url_b() != "") {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
                                HwUtil.TURNON();
                            }
                        }
                    }, 100);
                    //显示b面视频
                    //isplay = true;
                    String filename = FileUtil.getSDPath() + "/" + FILEGAOSUB + "/" + MD5Util.encrypt((String) advList.get(index).getMedia_url_b()) + ".mp4";
                    //index++;
                    boolean isExists = FileUtil.fileIsExists(filename);
                    if (isExists) {

                        iv_adv.setVisibility(View.INVISIBLE);
                        videoView.setVisibility(View.INVISIBLE);
                        topVideoView.setVisibility(View.VISIBLE);

                        topVideoView.setVideoPath(filename);
                        // videoView.setVideoPath(advList.get(index).getMedia_url());
                        topVideoView.start();
                        topVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                //isplay = false;

                                // advHandler.sendEmptyMessage(1);
                            }
                        });
                        //播放异常
                        topVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                topVideoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                                //advHandler.sendEmptyMessage(1);
                                return true;
                            }
                        });


                        advHandler.sendEmptyMessageDelayed(1, ADVTIME);
                    } else {

                        //index++;
                        advHandler.sendEmptyMessageDelayed(1, ADVTIME);

                    }

                } else {

                    HwUtil.TURNOFF();
                    //index++;
                    advHandler.sendEmptyMessageDelayed(1, ADVTIME);
                }

            }

            index++;
        }
    };

    @SuppressLint("HandlerLeak")
    public static Handler screenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //检查关机去掉

            if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
                if ("0".equals(HwUtil.GETST())) {
                    HwUtil.TURNON();
                    canturnon = true;
                }
                Log.i("aaa", "开机");

            } else {
                Log.i("aaa", "关机");
                HwUtil.TURNOFF();
                canturnon = false;
            }

            screenHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 5);


        }
    };

    @SuppressLint("HandlerLeak")
    public Handler tongbuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //

            advHandler.removeCallbacksAndMessages(null);
            index = 0;
            advHandler.sendEmptyMessage(1);

            Calendar now = Calendar.getInstance();

            int min = now.get(Calendar.MINUTE);
            int sec = now.get(Calendar.SECOND);
            int millsec = now.get(Calendar.MILLISECOND);
            int total = 60 * 60 * 1000 - (min * 60 * 1000 + sec * 1000 + millsec);

            tongbuHandler.sendEmptyMessageDelayed(1, total);
            AppUtil.NavigationBarStatusBar(MainActivity.this, true);
            // tongbuHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 60);

        }
    };

    //******************************handler*********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        AppUtil.NavigationBarStatusBar(this, true);
        setContentView(R.layout.activity_main);

        time = new Random().nextInt(30);
        //setTongbuTime();

        //time=0;
        INTERTIME = INTERTIME + time * 1000;
        initView();
        initClick();
        HwUtil.TURNON();
        // hideBar();

        registSreccnReceiver();
        setAlarm();
        updateHandler.sendEmptyMessageDelayed(1, INTERTIME);
        //updateHandler.sendEmptyMessageDelayed(1, 2000);


        checkScreenHandler.sendEmptyMessageDelayed(1, 2000);
        screenHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 5);

        FileUtil.makedir(FILEGAOSU);
        FileUtil.makedir(FILEGAOSUB);
        //init();

        //oncreatHandler.sendEmptyMessageDelayed(1, 1000 * 90 * 1 + time * 1000);
        oncreatHandler.sendEmptyMessage(1);
        heartHandler.sendEmptyMessageDelayed(1, 1000 * 3);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        //if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                        videoView.setBackgroundColor(Color.TRANSPARENT);
                        return true;

                    }
                });
            }
        });
        //banshouDonghua();


    }

    private void initView() {
        ivBanshou = findViewById(R.id.ivbanshou);
        tvtest = findViewById(R.id.tv_test);
        tvtestyj = findViewById(R.id.tv_test2);
        mask = findViewById(R.id.mask);
        videoView = findViewById(R.id.video);
        topVideoView = findViewById(R.id.topvideo);
        relativeLayout = findViewById(R.id.rl);
        toprelativeLayout = findViewById(R.id.toprl);
        iv_ewm = findViewById(R.id.iv_ewm);
        iv_adv = findViewById(R.id.iv_ad);
        tv_ewm = findViewById(R.id.tv_ewm);
        tv_banben = findViewById(R.id.tv_banben);
        tv_version = findViewById(R.id.tv_version);
        ll_info = findViewById(R.id.ll_info);
        tv_code = findViewById(R.id.tv_code);
        iv_top = findViewById(R.id.iv_top);
        btkj = findViewById(R.id.kj);
        btyl = findViewById(R.id.yl);
        try {
            versionCode = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionCode;
            tv_code.setText("软件版本" + versionCode);
            tv_banben.setText("软件版本" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tv_ewm.setText(Constant.MAC_ADDRESS);
        iv_ewm.setImageBitmap(ErweimaUtil.createQRcodeImage(Constant.MAC_ADDRESS));
        tv_version.setText("ExMIRO高速镜网©v" + versionCode);


        et = findViewById(R.id.et);
        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenLightUtil.setBrightness(MainActivity.this, Integer.parseInt(et.getText().toString()));
            }
        });
        btkj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // kaojin();
                boolean inDate = TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME);
                int a1 = 1;
            }
        });
        btyl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuanli();
            }
        });
    }

    private void init() {
        Map<String, String> map = new HashMap<>();

        map.put("mac_number", Constant.MAC_ADDRESS);
        //map.put("mac_number", "1111");
        map.put("model", "1");
        map.put("app_version", versionCode + "");
        OkHttpUtils.get().url(Url.INIT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Toast.makeText(MainActivity.this, "初始化异常...", Toast.LENGTH_LONG).show();


                try {
                    Object aDefault = SharedPreferencesUtil.getData("default", "");
                    Gson gson = new Gson();
                    if (aDefault != null && aDefault != "") {
                        List<ADVEntity.DataBean> list = gson.fromJson((String) aDefault, new TypeToken<List<ADVEntity.DataBean>>() {
                        }.getType());

                        if (list != null && list.size() > 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    advList = list;
                                    TOTALADV = list.size();
                                    index = 0;
                                    advHandler.removeCallbacksAndMessages(null);
                                    advHandler.sendEmptyMessage(1);


                                }
                            }, LOADADVTIME);
                        }


                    }


                    Log.i("qqq", "获取昨天广告");
                } catch (Exception ex) {
                    Log.i("qqq", "获取昨天广告异常");
                }

                oncreatHandler.sendEmptyMessageDelayed(1, 1000 * 60 * 5 + time * 1000);
            }

            @Override
            public void onResponse(String response, int id) {


                try {
                    Gson gson = new Gson();
                    InitEntity advEntity = gson.fromJson(response, InitEntity.class);
                    if (advEntity.getErrorCode() == 0) {
                        if (null != advEntity.getData().getStart_time()) {
                            Constant.STARTTIME = advEntity.getData().getStart_time();
                        }
                        if (null != advEntity.getData().getEnd_time()) {
                            Constant.ENDTIME = advEntity.getData().getEnd_time();
                        }
                        if (null != advEntity.getData().getCarousel_time()) {
                            ADVTIME = Integer.parseInt(advEntity.getData().getCarousel_time()) * 1000;
                            //ADVTIME = 5000;
                        }

                        TOTALADV = Integer.parseInt(advEntity.getData().getAd_number());
                        //TOTALADV = 3;

                    }
                    getadv();


                } catch (Exception e) {
                    e.printStackTrace();
                    getadv();
                    Toast.makeText(MainActivity.this, "init失败", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    @Override
    public void kaojin() {
        isKaojin = true;
        if (advList.size() == 0) {
            HwUtil.TURNOFF();
            return;
        }
        int curIndex = index - 1;
        if (curIndex < 0) {
            curIndex = advList.size() - 1;

        }

        if (advList.get(curIndex).getId() != "-1" && advList.get(curIndex).getType_b() != null && advList.get(curIndex).getType_b().equals("1")
                && advList.get(curIndex).getMedia_url_b() != null && advList.get(curIndex).getMedia_url_b() != "") {
            HwUtil.TURNON();
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(iv_adv.getDrawable());
            Glide.with(iv_adv.getContext()).load(advList.get(curIndex).getMedia_url_b()).apply(requestOptions).into(iv_adv);
            //切换图片

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_adv.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.INVISIBLE);

                }
            }, 300);

            //videoView.stopPlayback();


        } else if (advList.get(curIndex).getId() != "-1" && advList.get(curIndex).getType_b() != null && advList.get(curIndex).getType_b().equals("2")
                && advList.get(curIndex).getMedia_url_b() != null && advList.get(curIndex).getMedia_url_b() != "") {
            HwUtil.TURNON();
            //isplay = true;
            String filename = FileUtil.getSDPath() + "/" + FILEGAOSUB + "/" + MD5Util.encrypt((String) advList.get(curIndex).getMedia_url_b()) + ".mp4";

            boolean isExists = FileUtil.fileIsExists(filename);
            if (isExists) {

                iv_adv.setVisibility(View.INVISIBLE);


                topVideoView.setVideoPath(filename);
                // videoView.setVideoPath(advList.get(index).getMedia_url());
                topVideoView.start();
                topVideoView.setVisibility(View.VISIBLE);
                topVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //isplay = false;

                    }
                });
                //播放异常
                topVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        topVideoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞

                        return true;
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //topVideoView.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.INVISIBLE);


                    }
                }, 100);
                // videoView.setVisibility(View.INVISIBLE);
                //  videoView.setVisibility(View.INVISIBLE);


            } else {


            }
        } else {
            HwUtil.TURNOFF();

        }

    }

    @Override
    public void yuanli() {
        isKaojin = false;
        if (advList.size() == 0) {
            HwUtil.TURNON();
            return;
        }
        int curIndex = index - 1;
        if (curIndex < 0) {
            curIndex = advList.size() - 1;
        }
        if (advList.get(curIndex).getType().equals("1")) {

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(iv_adv.getDrawable());
            Glide.with(iv_adv.getContext()).load(advList.get(curIndex).getMedia_url()).apply(requestOptions).into(iv_adv);
            //延迟切换图片

            //iv_adv.setVisibility(View.VISIBLE);
//            videoView.setVisibility(View.INVISIBLE);
//            videoView.stopPlayback();
//            topVideoView.setVisibility(View.INVISIBLE);
//            topVideoView.stopPlayback();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoView.setVisibility(View.INVISIBLE);
                    videoView.stopPlayback();
                    topVideoView.setVisibility(View.INVISIBLE);
                    topVideoView.stopPlayback();
                    iv_adv.setVisibility(View.VISIBLE);
                    HwUtil.TURNON();
                }
            }, 100);

        } else if (advList.get(curIndex).getType().equals("2")) {

            String filename = FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + MD5Util.encrypt(advList.get(curIndex).getMedia_url()) + ".mp4";

            boolean isExists = FileUtil.fileIsExists(filename);
            if (isExists) {

                iv_adv.setVisibility(View.INVISIBLE);
                topVideoView.stopPlayback();

                videoView.setVisibility(View.VISIBLE);


                videoView.setVideoPath(filename);
                videoView.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        topVideoView.setVisibility(View.INVISIBLE);
                        int duration = videoView.getDuration();
                        if (duration < videotime * 1000 + 900) {
                            videoView.seekTo(duration - 300);
                        } else {
                            videoView.seekTo(videotime * 1000 - 300);
                        }

                        HwUtil.TURNON();
                    }
                }, 100);


            } else {


            }
        } else {
            HwUtil.TURNOFF();

        }

    }

    private void kaojinImp() {
        if (canOFF) {
            canOFF = false;
            ViewCompat.animate(mask)
                    .alpha(0.8f)
                    .setDuration(200)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            canOFF = true;
                            HwUtil.TURNOFF();
                            if ("0".equals(HwUtil.GETST())) {
                                yuanli();

                            }
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    })
                    .start();
//            ViewCompat.animate(relativeLayout)
//                    .scaleYBy(460)
//                    .setDuration(500)
//                    .setListener(new ViewPropertyAnimatorListener() {
//                        @Override
//                        public void onAnimationStart(View view) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(View view) {
//                            canOFF = true;
//                            HwUtil.TURNOFF();
//                            if ("0".equals(HwUtil.GETST())) {
//                                yuanli();
//
//                            }
//                        }
//
//                        @Override
//                        public void onAnimationCancel(View view) {
//
//                        }
//                    })
//                    .start();
        }

    }

    private void yuanliImp() {
        if (canON) {
            canON = false;
            HwUtil.TURNON();

            ViewCompat.animate(mask)
                    .alpha(0)
                    .setDuration(200)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            canON = true;
                            if ("1".equals(HwUtil.GETST())) {
                                kaojin();

                            }
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    })
                    .start();
        }
    }

    private void fd() {
        Log.i("xxx", "relativeLayout.getHeight()" + relativeLayout.getHeight());
        ViewCompat.animate(relativeLayout)
                .setDuration(200)
                .scaleY(1f)
                .scaleX(1f)
                .translationY(0)
                .translationX(0)
                .start();

    }

    private void sx() {

        Log.i("xxx", "relativeLayout.getHeight()" + relativeLayout.getHeight());
        ViewCompat.animate(relativeLayout)
                .setDuration(200)
                .scaleY(0.33f)
                .scaleX(0.33f)


                .translationY(-relativeLayout.getHeight() / 3)
                .translationX(-relativeLayout.getWidth() / 3)
                .start();

    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);


        if (m < 15) {
            m = 15;
        } else if (m < 30) {
            m = 30;
        } else if (m < 45) {
            m = 45;
        } else if (m < 60) {
            if (h == 23) {
                calendar.add(Calendar.DATE, 1);
                h = 0;
            } else {
                h = h + 1;
            }

            m = 0;
        }

        Toast.makeText(MainActivity.this, "h=" + h + "m=" + m, Toast.LENGTH_LONG).show();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 建立Intent和PendingIntent来调用目标组件
        Intent intent = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
        //intent.setAction("qyf");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        // 获取闹钟管理的实例
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        long timeInMillis = calendar.getTimeInMillis();
        //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.i("ggg", "h=" + h + "m=" + m + "time=" + timeInMillis);
        tvtest.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        tvtestyj.setText("预计" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTimeInMillis()));

        //am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        // am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*30,pendingIntent);
        //AlarmManager.in
    }

    private void setTongbuTime() {
        Calendar now = Calendar.getInstance();

        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        int millsec = now.get(Calendar.MILLISECOND);
        int total = 60 * 60 * 1000 - (min * 60 * 1000 + sec * 1000 + millsec);
        tongbuHandler.sendEmptyMessageDelayed(1, total);

    }

    private void sendHeart() {
        Map<String, String> map = new HashMap<>();

        map.put("mac_number", Constant.MAC_ADDRESS);
        if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
            map.put("is_standby", "0");
        } else {
            map.put("is_standby", "1");
        }


        OkHttpUtils.get().url(Url.HEART).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ivBanshou.setVisibility(View.VISIBLE);

                ivBanshou.setBackgroundResource(R.drawable.xbsmr);
                banshouDonghua();
            }

            @Override
            public void onResponse(String response, int id) {
                ivBanshou.clearAnimation();
                ivBanshou.setVisibility(View.GONE);

            }
        });
    }

    private void getadv() {
        Map<String, String> map = new HashMap<>();

        map.put("mac_number", Constant.MAC_ADDRESS);

        OkHttpUtils.get().url(Url.ADV).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Toast.makeText(MainActivity.this, "网络异常...", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResponse(String response, int id) {


                try {
                    Gson gson = new Gson();
                    ADVEntity advEntity = gson.fromJson(response, ADVEntity.class);
                    List<ADVEntity.DataBean> data = advEntity.getData();

                    for (int i = 0; i < data.size(); i++) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            Date dateStart = sdf.parse(data.get(i).getStart_at());
                            Date dateEnd = sdf.parse(data.get(i).getEnd_at());
                            Date curDate = new Date();
                            if (curDate.getTime() < dateStart.getTime() || curDate.getTime() > dateEnd.getTime()) {

                                data.remove(i);
                                i--;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();

                        }
                    }
                    //
                    if (data.size() > 0) {
                        AdvSortUtil.saveData(data, data.size());
                    }

                    if (data.size() < TOTALADV) {
                        OkHttpUtils.get().url(Url.ADVDEFAULT).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Gson gson = new Gson();
                                ADVEntity advEntity = gson.fromJson(response, ADVEntity.class);
                                List<ADVEntity.DataBean> defaultAdv = advEntity.getData();
                                int num = TOTALADV - data.size();
                                if (defaultAdv.size() >= num) {
                                    for (int i = 0; i < num; i++) {
                                        data.add(defaultAdv.get(i));
                                    }
                                }
                                //保存默认广告
                                AdvSortUtil.saveDefaultData(data, data.size());
                                //下载视频
                                for (ADVEntity.DataBean dataBean : data) {
                                    //下载a面
                                    if (dataBean.getId() != "-1" && dataBean.getType().equals("2")) {
                                        String filename = FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + MD5Util.encrypt(dataBean.getMedia_url()) + ".mp4";
                                        boolean isExists = FileUtil.fileIsExists(filename);
                                        if (!isExists) {
                                            //查看文件个数
                                            File file = new File(FileUtil.getSDPath() + "/" + FILEGAOSU);
                                            File[] listFiles = file.listFiles();
                                            long time = 0;
                                            if (listFiles.length > 100) {
                                                for (int i = 0; i < listFiles.length; i++) {
                                                    if (i == 0) {
                                                        time = listFiles[0].lastModified();
                                                    } else {
                                                        if (time > listFiles[i].lastModified()) {
                                                            time = listFiles[i].lastModified();
                                                        }
                                                    }
                                                }
                                            }
                                            for (File file1 : listFiles) {
                                                if (file1.lastModified() == time) {

                                                    FileUtil.deleteFile(file1.getAbsolutePath());
                                                }
                                            }

                                            //
                                            downloadFile(dataBean.getMedia_url(), MD5Util.encrypt(dataBean.getMedia_url()) + ".mp4");
                                            //downloadFile(dataBean.getMedia_url(), dataBean.getId() + ".mp4");
                                        } else {
                                            Log.i("aaaa", "广告存在，bu下载");
                                        }

                                    }
                                    //下载b面
                                    Log.i("ffff", "下载b面次数");
                                    if (dataBean.getId() != "-1" && dataBean.getType_b() != null && dataBean.getType_b().equals("2")
                                            && (String) dataBean.getMedia_url_b() != "") {

                                        String filename = FileUtil.getSDPath() + "/" + FILEGAOSUB + "/" + MD5Util.encrypt((String) dataBean.getMedia_url_b()) + ".mp4";
                                        Log.i("ffff", "下载b面" + filename);
                                        boolean isExists = FileUtil.fileIsExists(filename);
                                        if (!isExists) {
                                            //查看文件个数
                                            File file = new File(FileUtil.getSDPath() + "/" + FILEGAOSUB);
                                            File[] listFiles = file.listFiles();
                                            long time = 0;
                                            if (listFiles.length > 100) {
                                                for (int i = 0; i < listFiles.length; i++) {
                                                    if (i == 0) {
                                                        time = listFiles[0].lastModified();
                                                    } else {
                                                        if (time > listFiles[i].lastModified()) {
                                                            time = listFiles[i].lastModified();
                                                        }
                                                    }
                                                }
                                            }
                                            for (File file1 : listFiles) {
                                                if (file1.lastModified() == time) {

                                                    FileUtil.deleteFile(file1.getAbsolutePath());
                                                }
                                            }

                                            //
                                            Log.i("bbbbb", "原始" + (String) dataBean.getMedia_url_b());
                                            downloadFileB((String) dataBean.getMedia_url_b(), MD5Util.encrypt((String) dataBean.getMedia_url_b()) + ".mp4");

                                        } else {
                                            Log.i("aaaa", "广告存在，bu下载");
                                        }

                                    }
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (data.size() > 0) {

                                            index = 0;
                                            //重新排序
                                            if (data.size() == TOTALADV) {
                                                advList = data;
                                            } else {
                                                advList = AdvSortUtil.sortAdv(data, TOTALADV);
                                            }
                                            //advList = data;
                                            advHandler.removeCallbacksAndMessages(null);
                                            advHandler.sendEmptyMessage(1);
                                        }


                                    }
                                }, LOADADVTIME);


                            }
                        });
                    } else {
                        //保存默认广告
                        AdvSortUtil.saveDefaultData(data, data.size());
                        for (ADVEntity.DataBean dataBean : data) {
                            //下载a面
                            //String filename = FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + MD5Util.encrypt(advList.get(index).getMedia_url()) + ".mp4";

                            if (dataBean.getId() != "-1" && dataBean.getType().equals("2")) {
                                String filename = FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + MD5Util.encrypt(dataBean.getMedia_url()) + ".mp4";
                                boolean isExists = FileUtil.fileIsExists(filename);
                                if (!isExists) {
                                    //查看文件个数
                                    File file = new File(FileUtil.getSDPath() + "/" + FILEGAOSU);
                                    File[] listFiles = file.listFiles();
                                    long time = 0;
                                    if (listFiles.length > 100) {
                                        for (int i = 0; i < listFiles.length; i++) {
                                            if (i == 0) {
                                                time = listFiles[0].lastModified();
                                            } else {
                                                if (time > listFiles[i].lastModified()) {
                                                    time = listFiles[i].lastModified();
                                                }
                                            }
                                        }
                                    }
                                    for (File file1 : listFiles) {
                                        if (file1.lastModified() == time) {

                                            FileUtil.deleteFile(file1.getAbsolutePath());
                                        }
                                    }

                                    //
                                    downloadFile(dataBean.getMedia_url(), MD5Util.encrypt(dataBean.getMedia_url()) + ".mp4");

                                } else {
                                    Log.i("aaaa", "广告存在，bu下载");
                                }

                            }
                            //下载b面

                            if (dataBean.getId() != "-1" && dataBean.getType_b() != null && dataBean.getType_b().equals("2")
                                    && (String) dataBean.getMedia_url_b() != "") {

                                String filename = FileUtil.getSDPath() + "/" + FILEGAOSUB + "/" + MD5Util.encrypt((String) dataBean.getMedia_url_b()) + ".mp4";
                                Log.i("ffff", "下载b面" + filename);
                                boolean isExists = FileUtil.fileIsExists(filename);
                                if (!isExists) {
                                    //查看文件个数
                                    File file = new File(FileUtil.getSDPath() + "/" + FILEGAOSUB);
                                    File[] listFiles = file.listFiles();
                                    long time = 0;
                                    if (listFiles.length > 100) {
                                        for (int i = 0; i < listFiles.length; i++) {
                                            if (i == 0) {
                                                time = listFiles[0].lastModified();
                                            } else {
                                                if (time > listFiles[i].lastModified()) {
                                                    time = listFiles[i].lastModified();
                                                }
                                            }
                                        }
                                    }
                                    for (File file1 : listFiles) {
                                        if (file1.lastModified() == time) {

                                            FileUtil.deleteFile(file1.getAbsolutePath());
                                        }
                                    }

                                    //
                                    Log.i("bbbbb", "原始" + (String) dataBean.getMedia_url_b());
                                    downloadFileB((String) dataBean.getMedia_url_b(), MD5Util.encrypt((String) dataBean.getMedia_url_b()) + ".mp4");

                                } else {
                                    Log.i("aaaa", "广告存在，bu下载");
                                }

                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (data.size() > 0) {

                                    index = 0;
                                    //重新排序
                                    if (data.size() == TOTALADV) {
                                        advList = data;
                                    } else {
                                        advList = AdvSortUtil.sortAdv(data, TOTALADV);
                                    }
                                    //advList = data;
                                    advHandler.removeCallbacksAndMessages(null);
                                    advHandler.sendEmptyMessage(1);
                                }


                            }
                        }, LOADADVTIME);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "获取广告失败", Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    public void downloadFile(String url, String houZuiName) {
        Toast.makeText(MainActivity.this, "开始下载广告", Toast.LENGTH_SHORT).show();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(FileUtil.getSDPath() + "/" + FILEGAOSU + "/", houZuiName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        FileUtil.deleteFile(FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + houZuiName);
                        downloadFile(url, houZuiName);


                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                    }

                    @Override
                    public void onResponse(File downloadFile, int id) {


                    }
                });
    }

    public void downloadFileB(String url, String houZuiName) {
        Toast.makeText(MainActivity.this, "开始下载广告", Toast.LENGTH_SHORT).show();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(FileUtil.getSDPath() + "/" + FILEGAOSUB + "/", houZuiName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        FileUtil.deleteFile(FileUtil.getSDPath() + "/" + FILEGAOSUB + "/" + houZuiName);
                        downloadFileB(url, houZuiName);

                        Toast.makeText(MainActivity.this, "下载广告失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                    }

                    @Override
                    public void onResponse(File downloadFile, int id) {
                        Toast.makeText(MainActivity.this, "下载广告成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void banshouDonghua() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.banshou);
        ivBanshou.startAnimation(animation);

    }

    private void initClick() {
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();

//
//                    Log.i("xxx", "relativeLayout.getHeight()" + relativeLayout.getHeight());
//                    ViewCompat.animate(relativeLayout)
//                            .setDuration(200)
//                            .scaleY(0.25f)
//
//
//
//                            .translationY(-relativeLayout.getHeight() / 4*3/2)
//
//                            .start();

            }
        });
        ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_info.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {


        super.onResume();
        //init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacksAndMessages(null);
        advHandler.removeCallbacksAndMessages(null);

        screenHandler.removeCallbacksAndMessages(null);
        checkScreenHandler.removeCallbacksAndMessages(null);
        tongbuHandler.removeCallbacksAndMessages(null);
        oncreatHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(screenReciver);
        unregisterReceiver(netWorkChangReceiver);
        unregisterReceiver(alarmBroadcastReceiver);
    }

    void checkUpdate() {
        Map<String, String> map = new HashMap<>();
        // map.put("type", "4");
        OkHttpUtils.get().url(Url.UPDATE_VERSION).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //  Toast.makeText(MainActivity.this, "更新异常...", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResponse(String response, int id) {
                try {

                    Gson gson = new Gson();
                    UpdateEntity updateEntity = gson.fromJson(response, UpdateEntity.class);


                    if (versionCode < updateEntity.getVersion()) {
                        Toast.makeText(MainActivity.this, "检测到新版本，开始更新...", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, DownMOLIVideoService.class);
                        intent.putExtra("url", updateEntity.getDownload());
                        intent.putExtra("versionCode", versionCode);
                        intent.putExtra("size", updateEntity.getFile_size());
                        startService(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "当前是最新版本", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "检查更新失败", Toast.LENGTH_LONG).show();

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
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
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

    private void showListDialog() {
        final String[] items = {"设置WIFI", "显示mac地址", "检查更新", "启动系统页"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(MainActivity.this);
        listDialog.setTitle("选项");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
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

    void registSreccnReceiver() {
        //pingmu
        screenReciver = new ScreenReciver();
        screenReciver.setListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HOOK_EVENT");
        registerReceiver(screenReciver, intentFilter);
        //wifi
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netWorkChangReceiver.setWifiCallback(this);
        registerReceiver(netWorkChangReceiver, filter);
        //
        alarmBroadcastReceiver = new AlarmBroadcastReceiver();

        IntentFilter afilter = new IntentFilter();
        afilter.addAction("android.intent.QYF");

        registerReceiver(alarmBroadcastReceiver, afilter);


    }

    @Override
    public void wifiCallback() {

        //init();

    }

    public static class AlarmBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("ggg", "收到时钟广播");

            Calendar calendar = Calendar.getInstance();
            int m = calendar.get(Calendar.MINUTE);

            advHandler.removeCallbacksAndMessages(null);
            index = 0;
            advHandler.sendEmptyMessage(1);
            tvtest.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTimeInMillis()));
            Toast.makeText(context, "同步完成", Toast.LENGTH_LONG).show();

            setAlarm2(context);

            // setAlarm2(context);

        }


        void setAlarm2(Context context) {
            Calendar calendar = Calendar.getInstance();
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);


            if (m < 15) {
                m = 15;
            } else if (m < 30) {
                m = 30;
            } else if (m < 45) {
                m = 45;
            } else if (m < 60) {
                if (h == 23) {
                    calendar.add(Calendar.DATE, 1);
                    h = 0;
                } else {
                    h = h + 1;
                }

                m = 0;
            }

            Toast.makeText(context, "h=" + h + "m=" + m, Toast.LENGTH_LONG).show();

            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, h);
            calendar.set(Calendar.MINUTE, m);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            // 建立Intent和PendingIntent来调用目标组件
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            //intent.setAction("qyf");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // 获取闹钟管理的实例
            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            long timeInMillis = calendar.getTimeInMillis();
            //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.i("ggg", "h=" + h + "m=" + m + "time=" + timeInMillis);
            tvtest.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            tvtestyj.setText("预计" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTimeInMillis()));

            //am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            //am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*30,pendingIntent);

        }
    }

}
