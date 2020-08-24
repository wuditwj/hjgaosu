package com.gaosu.heijing.entity;

import java.util.List;

public class TestE {

    /**
     * errorCode : 0
     * message : ok
     * data : [{"id":"307","ispublic":"0","start_at":"2020-02-27","end_at":"2020-06-01","type":"1","media_url":"http://192.168.0.202/advertisement/2020022700307/5e572ceca5198.jpg","type_b":null,"media_url_b":null},{"id":"308","ispublic":"0","start_at":"2020-02-27","end_at":"2020-06-01","type":"1","media_url":"http://192.168.0.202/advertisement/2020022700308/5e572cfe72e02.jpg","type_b":"2","media_url_b":""},{"id":"309","ispublic":"0","start_at":"2020-02-27","end_at":"2020-04-01","type":"1","media_url":"http://192.168.0.202/advertisement/2020022700309/5e57309c43da9.jpg","type_b":"1","media_url_b":"advertisement/2020010600298/5e12f90b18120.jpg"}]
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
         * id : 307
         * ispublic : 0
         * start_at : 2020-02-27
         * end_at : 2020-06-01
         * type : 1
         * media_url : http://192.168.0.202/advertisement/2020022700307/5e572ceca5198.jpg
         * type_b : null
         * media_url_b : null
         */

        private String id;
        private String ispublic;
        private String start_at;
        private String end_at;
        private String type;
        private String media_url;
        private Object type_b;
        private Object media_url_b;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIspublic() {
            return ispublic;
        }

        public void setIspublic(String ispublic) {
            this.ispublic = ispublic;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public Object getType_b() {
            return type_b;
        }

        public void setType_b(Object type_b) {
            this.type_b = type_b;
        }

        public Object getMedia_url_b() {
            return media_url_b;
        }

        public void setMedia_url_b(Object media_url_b) {
            this.media_url_b = media_url_b;
        }
    }
}
