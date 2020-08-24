package com.gaosu.heijing.util;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gaosu.heijing.entity.TopAdvEntity;

import java.util.List;
import java.util.Random;

import static com.gaosu.heijing.MainActivity.FILEGAOSU;

public class TopadvUtil {
    public static int topflag = 1;
    public static void showTopAdv(List<TopAdvEntity.DataBean> topadvlist, ImageView iv_top, VideoView topVideoView  ){
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .placeholder(iv_top.getDrawable());
        int i = 0;
        if (topadvlist.size() == 1) {
            i = 0;
        } else {
            i = new Random().nextInt(topadvlist.size());
        }
        i=0;
        topflag = topadvlist.get(i).getType();
        if (topadvlist.get(i).getMedia_url().endsWith("gif")) {

            Glide.with(iv_top.getContext()).load(topadvlist.get(i).getMedia_url()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                    if (drawable instanceof GifDrawable) {
                        GifDrawable gifDrawable = (GifDrawable) drawable;
                        gifDrawable.setLoopCount(999);
                        iv_top.setImageDrawable(drawable);
                        gifDrawable.start();
                    }
                }
            });
        } else if (topadvlist.get(i).getType() == 1) {

            Glide.with(iv_top.getContext()).load(topadvlist.get(0).getMedia_url()).apply(requestOptions).into(iv_top);

        } else if (topadvlist.get(i).getType() == 2) {
            String filename = FileUtil.getSDPath() + "/" + FILEGAOSU + "/" + MD5Util.encrypt(topadvlist.get(i).getMedia_url()) + ".mp4";

            boolean isExists = FileUtil.fileIsExists(filename);
            if (isExists) {

                topVideoView.setVideoPath(filename);
                // videoView.setVideoPath(advList.get(index).getMedia_url());
                //topVideoView.start();
                topVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // topVideoView.start();

                        // advHandler.sendEmptyMessage(1);
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

            }

        }


    }
}
