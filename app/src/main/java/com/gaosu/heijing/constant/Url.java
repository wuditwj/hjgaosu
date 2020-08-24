package com.gaosu.heijing.constant;

/**
 * 请求地址
 * Created by LTY98 on 2017/10/17.
 */

public class Url {


    //public static String URL_ROOT = "http://192.168.0.202/api/";//测试地址


    public static String URL_ROOT = "http://exmiroserver.mirrormedia.cn/api/";//主站地址
    public static String UPDATE_VERSION = URL_ROOT + "basic/update?type=10";

    public static String ADV = URL_ROOT + "eq/ad/siteadlist";
    public static String INIT = URL_ROOT + "eq/system/init";
    public static String HEART = URL_ROOT + "eq/system/heartbeat";
    public static String ADVDEFAULT = URL_ROOT + "eq/ad/sitedefaultadlist";

    public static String UPLOADPLAY = URL_ROOT + "eq/record/adrecord";
    public static String TOPADV= URL_ROOT + "eq/ad/sitedefaultadlisthalf";
    public static String BAIDU="https://www.baidu.com/";

}
