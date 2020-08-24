package com.gaosu.heijing.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.gaosu.heijing.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 检测到更新后，在此服务中做下载apk的操作
 * Created by LTY98 on 2017/11/25.
 */

public class DownMOLIVideoService extends Service {
    private int versionCode;
    private boolean isFirst = false; //是否第一次调用startService;
    private Iservice iservice;

    @Override
    public void onCreate() {
        super.onCreate();
        isFirst = true;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (isFirst) {
            downLoadApk(intent.getStringExtra("url"), intent.getStringExtra("size"));
            versionCode = intent.getIntExtra("versionCode", 0);
            isFirst = false;//置为false，不用每次都做下载操作
        }

        //
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk(final String url, final String size) {
//
        new Thread() {
            @Override
            public void run() {
                try {
//                    Log.e("PPPP", "download");
                    File file = DownLoadManager.getMOLIFileFromServer(url);
                    sleep(3000);
                    stopSelf();
                    installApk(file, size);
                } catch (Exception e) {
                    Looper.prepare();

//                    Log.e("pppp", "下载失败");
                    e.printStackTrace();
                    isFirst = true;
                    Looper.loop();
                }
            }
        }.start();
    }

    private void installApk(File file, String file_size) {
        Process process = null;
        OutputStream out = null;
        Log.e("aaaa", "开始1");
        try {
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            String path = Environment.getExternalStorageDirectory() + File.separator + "agaosu.apk";

            Log.e("aaaa", "开始2");
//            if (file_size.equals(new File(path).length() + "")) {//判断下载的安装包是否和服务器上的安装包大小一致
//                // 调用安装
//                Log.e("aaaa", "开始3");
//                out.write((("pm install -r " + path) + "\n").getBytes());
//            } else {
//                Log.e("aaaa", "开始4");
//            }

            out.write((("pm install -r " + path) + "\n").getBytes());
        } catch (IOException e) {
            Log.e("aaaa", "失败2");
            e.printStackTrace();
        }
    }


    public interface Iservice {
        void show(String s);
    }
}
