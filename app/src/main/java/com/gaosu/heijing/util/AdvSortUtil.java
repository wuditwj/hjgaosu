package com.gaosu.heijing.util;

import android.util.Log;

import com.gaosu.heijing.constant.Constant;
import com.gaosu.heijing.constant.Url;
import com.gaosu.heijing.entity.ADVEntity;
import com.gaosu.heijing.entity.PlayEntity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

public class AdvSortUtil {
    public static List<ADVEntity.DataBean> sortAdv(List<ADVEntity.DataBean> oldList, int total) {
        List<ADVEntity.DataBean> newList = new ArrayList<>();
        List<ADVEntity.DataBean> shangyeList = new ArrayList<>();
        List<ADVEntity.DataBean> gongyiList = new ArrayList<>();
        List<ADVEntity.DataBean> pingtaiList = new ArrayList<>();

        for (ADVEntity.DataBean bean : oldList) {
            if (bean.getIspublic().equals("0")) {
                shangyeList.add(bean);
            } else if (bean.getIspublic().equals("1")) {
                gongyiList.add(bean);
            } else if (bean.getIspublic().equals("2")) {
                pingtaiList.add(bean);
            }
        }


        if (gongyiList.size() > 0) {
            Collections.reverse(gongyiList);
        }
        if (pingtaiList.size() > 0) {
            Collections.reverse(pingtaiList);
        }

        newList.addAll(shangyeList);
        newList.addAll(gongyiList);
        newList.addAll(pingtaiList);

        //保存数据
        saveData(newList, total);


        newList = newList.subList(0, total);
        return newList;

    }

    public static void saveData(List<ADVEntity.DataBean> newList, int total) {
        List<PlayEntity> playList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < newList.size(); i++) {
            PlayEntity entity = new PlayEntity();
            entity.setAd_id(Integer.parseInt(newList.get(i).getId()));
            entity.setDate(simpleDateFormat.format(new Date()));
            if (i < total) {
                entity.setIs_play(1);
            } else {
                entity.setIs_play(0);
            }
            playList.add(entity);

        }


        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
        int today = Integer.parseInt(simpleDateFormat2.format(new Date()));
        HashMap<String, PlayEntity> newhashMap = new HashMap<String, PlayEntity>();
        for (PlayEntity entity : playList) {
            newhashMap.put(entity.getAd_id() + "", entity);
        }
        HashMap<String, PlayEntity> oldMapData = new HashMap<String, PlayEntity>();
        HashMap<String, PlayEntity> json = SharedPreferencesUtil.getHashMapData(today + "", PlayEntity.class);
        if (null != json) {
            oldMapData.putAll(newhashMap);
            Log.i("aaa", "有数据");
        } else {
            Log.i("aaa", "没数据");
        }

        SharedPreferencesUtil.putHashMapData(today + "", oldMapData);

        //获取昨天的数据
        List<PlayEntity> yestadayList = new ArrayList<>();
        String yestaday = Integer.parseInt(simpleDateFormat2.format(new Date().getTime() - 1000 * 60 * 60 * 24l)) + "";
        HashMap<String, PlayEntity> hashMapData = SharedPreferencesUtil.getHashMapData(yestaday, PlayEntity.class);
        if (hashMapData != null) {
            Set<String> keySet = hashMapData.keySet();
            for (String key : keySet) {
                //通过key，获取value：
                PlayEntity value = hashMapData.get(key);
                yestadayList.add(value);
            }
            Gson gson = new Gson();
            String yestadayString = gson.toJson(yestadayList);
            Map<String, String> map = new HashMap<>();

            map.put("mac_number", Constant.MAC_ADDRESS);
            map.put("recordlist", yestadayString);
            OkHttpUtils.get().url(Url.UPLOADPLAY).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    Log.i("aaa", "发送成功");
                    SharedPreferencesUtil.removeData(yestaday);
                }
            });
        }
    }

    public static void saveDefaultData(List<ADVEntity.DataBean> newList, int total) {
        Gson gson = new Gson();
        String defaultadv = gson.toJson(newList);
        SharedPreferencesUtil.putData("default", defaultadv);


    }




}

