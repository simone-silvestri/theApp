package com.simone.cfts;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {

    private String date;
    private ArrayList<Integer> wod;

    public History() {
        this.date = "NOWOD";
        this.wod = new ArrayList<Integer>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Integer> getWod() {
        return wod;
    }

    public void setWod(ArrayList<Integer> wod) {
        this.wod = wod;
    }
}
