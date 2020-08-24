package com.gaosu.heijing.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gaosu.heijing.R;
import com.gaosu.heijing.util.SystemUtils;

public class BaseActivity extends AppCompatActivity {
    /**
     * 检测网络,为true则有网
     */
    public Boolean checkNet() {
        if (SystemUtils.checkNet(this)) {
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到当前界面的装饰视图
//        View decorView = getWindow().getDecorView();
////        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
//        //设置系统UI元素的可见性
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
