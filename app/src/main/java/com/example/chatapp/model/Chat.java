package com.example.chatapp.model;

public class Chat {
    public String sender;
    public String reciver;
    public String message;
    public String time;
    public String tgl;
    public String sender_name;
    public boolean isseen;

    public Chat() {
    }

    public Chat(String sender, String reciver, String message, String time, String tgl, String sender_name,Boolean isseen) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
        this.time = time;
        this.tgl = tgl;
        this.sender_name = sender_name;
        this.isseen=isseen;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }


    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
