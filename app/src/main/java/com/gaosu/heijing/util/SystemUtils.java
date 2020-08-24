package com.gaosu.heijing.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 系统工具类
 * Created by Tiger on 2017/9/26.
 */

public class SystemUtils {
    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// 网络是否连接
    }

    /**
     * 获取当前应用程序版本号码
     * @return version
     */
    public static int getAppVersionCode(Context context) {
        int version;
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(SystemUtils.class.getName()
                    + "the application not found");
        }
        return version;
    }

    public  static void execShell(String cmd){
        try {
            Process p= Runtime.getRuntime().exec(new String[]{"su","-c",cmd});
            BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String readLine=br.readLine();
            while(readLine!=null){
                System.out.println(readLine);
                readLine=br.readLine();
            }
            if(br!=null){
                br.close();
            }
            p.destroy();
            p=null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
