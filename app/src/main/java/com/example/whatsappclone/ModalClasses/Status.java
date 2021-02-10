package com.example.whatsappclone.ModalClasses;

public class Status {
    private String imageUrl;
    private long time;

    public Status() {
    }

    public Status(String imageUrl, long time) {
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
