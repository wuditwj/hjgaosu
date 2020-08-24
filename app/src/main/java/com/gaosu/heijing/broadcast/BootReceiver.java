package com.gaosu.heijing.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gaosu.heijing.MainActivity;
import com.gaosu.heijing.MainActivity2;


/**
 * 开机自动启动应用广播
 * Created by Tiger on 2017/9/26.
 */

public class BootReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {

            Intent start = new Intent(context, MainActivity.class);
            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(start);

        }

    }
}
