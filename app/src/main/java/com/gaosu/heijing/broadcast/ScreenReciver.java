package com.gaosu.heijing.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.gaosu.heijing.constant.Constant;
import com.gaosu.heijing.util.HwUtil;
import com.gaosu.heijing.util.TimeUtil;

import java.util.Date;

public class ScreenReciver extends BroadcastReceiver {


    @SuppressLint("HandlerLeak")
    public Handler DELAYHANDLER = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CANCON = true;
//            if ("0".equals(HwUtil.GETST())) {
//                callback.yuanli();
//
//            }else {
//                callback.kaojin();
//            }


        }
    };


    public static boolean CANCON = true;

    public static int KAOJIN = 0;
    public static int YAUNLI = 1;


    @Override
    public void onReceive(Context context, Intent intent) {


        if (TimeUtil.isInDate(new Date(), Constant.STARTTIME, Constant.ENDTIME)) {
            String st = HwUtil.GETST();
            if (CANCON) {
                CANCON = false;

                if ("0".equals(st)) {
                    //开屏开屏,人远离
                    // HwUtil.TURNON();
                    // callback.fangda();
                    DELAYHANDLER.sendEmptyMessageDelayed(YAUNLI, 300);

                    callback.yuanli();


                } else if ("1".equals(st)) {
                    //关屏，人靠近
                    // HwUtil.TURNOFF();

                    DELAYHANDLER.sendEmptyMessageDelayed(KAOJIN, 300);

                    callback.kaojin();


                }
            }

        }
    }

    private Callback callback;

    public void setListener(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void kaojin();

        void yuanli();
    }


}
