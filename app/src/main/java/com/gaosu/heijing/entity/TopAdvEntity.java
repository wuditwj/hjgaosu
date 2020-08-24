package com.gaosu.heijing.entity;

import java.util.List;

public class TopAdvEntity {
    /**
     * errorCode : 0
     * message : 查询成功
     * data : [{"media_url":"http://192.168.0.202/advertisement/default/half/1/微信图片_20191008122909.jpg","type":1},{"media_url":"http://192.168.0.202/advertisement/default/half/1/微信图片_20191008122904.jpg","type":1},{"media_url":"http://192.168.0.202/advertisement/default/half/2/b0253314abbd27e00bff15392aad6e0d.mp4","type":2}]
     */

    private int errorCode;
    private String message;
    private List<DataBean> data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * media_url : http://192.168.0.202/advertisement/default/half/1/微信图片_20191008122909.jpg
         * type : 1
         */

        private String media_url;
        private int type;

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
