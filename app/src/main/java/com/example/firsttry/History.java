package com.example.firsttry;

import java.io.Serializable;

public class History implements Serializable {

    private long date;
    private int wod;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getWod() {
        return wod;
    }

    public void setWod(int wod) {
        this.wod = wod;
    }
}
