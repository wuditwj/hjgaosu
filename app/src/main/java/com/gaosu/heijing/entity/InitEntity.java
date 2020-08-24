package com.gaosu.heijing.entity;

public class InitEntity {

    /**
     * errorCode : 0
     * message : 初始化成功
     * data : {"start_time":"09:00:00","end_time":"17:00:00","carousel_time":"15","ad_numb":9,"public_number":3}
     */

    private int errorCode;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * start_time : 09:00:00
         * end_time : 17:00:00
         * carousel_time : 15
         * ad_numb : 9
         * public_number : 3
         */

        private String start_time;
        private String end_time;
        private String carousel_time;
        private int ad_numb;
        private int public_number;
        private String ad_number;

        public String getAd_number() {
            return ad_number;
        }

        public void setAd_number(String ad_number) {
            this.ad_number = ad_number;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getCarousel_time() {
            return carousel_time;
        }

        public void setCarousel_time(String carousel_time) {
            this.carousel_time = carousel_time;
        }

        public int getAd_numb() {
            return ad_numb;
        }

        public void setAd_numb(int ad_numb) {
            this.ad_numb = ad_numb;
        }

        public int getPublic_number() {
            return public_number;
        }

        public void setPublic_number(int public_number) {
            this.public_number = public_number;
        }
    }
}
