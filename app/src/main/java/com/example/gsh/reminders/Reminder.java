package com.example.gsh.reminders;

/**
 * Created by Administrator on 2016/11/21.
 */

public class Reminder {
    private int mid;
    private String mContent;
    private int mImportant;

    public Reminder(int id,String content,int important){
        mid=id;
        mContent=content;
        mImportant=important;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getImportant() {
        return mImportant;
    }

    public void setImportant(int important) {
        mImportant = important;
    }
}
