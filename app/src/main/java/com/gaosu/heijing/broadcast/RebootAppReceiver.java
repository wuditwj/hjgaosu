package com.gaosu.heijing.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.gaosu.heijing.MainActivity;
import com.gaosu.heijing.MainActivity2;


/**
 * 接受升级安装包的广播，用来做更新安装后自启app
 * Created by LTY98 on 2017/11/29.
 */

public class RebootAppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {

            Intent start = new Intent(context, MainActivity.class);
            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(start);


        }
    }
}
