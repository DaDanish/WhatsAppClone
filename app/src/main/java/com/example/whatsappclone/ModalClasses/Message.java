package com.example.whatsappclone.ModalClasses;

public class Message {
    private String message;
    private String messageId;
    private String senderId;
    private long time;

    public Message() {
    }

    public Message(String message, String senderId, long time) {
        this.message = message;
        this.senderId = senderId;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
