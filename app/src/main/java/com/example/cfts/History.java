package com.example.cfts;

import java.io.Serializable;

public class History implements Serializable {

    private String date;
    private int wod;

    public History() {
        this.date = "NOWOD";
        this.wod = -1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWod() {
        return wod;
    }

    public void setWod(int wod) {
        this.wod = wod;
    }
}
