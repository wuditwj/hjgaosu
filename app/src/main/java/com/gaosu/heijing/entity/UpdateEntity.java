package com.gaosu.heijing.entity;

import java.util.Date;

public class UpdateEntity {


    private int version;
    private String title;
    private String message;
    private String file_size;
    private String download;

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getDownload() {
        return download;
    }
}
