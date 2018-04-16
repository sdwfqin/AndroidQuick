package com.sdwfqin.baidumaplib.utils;

import java.io.Serializable;

/**
 * Created by sdwfqin on 2017/7/27.
 */
public class MarkerBean implements Serializable {

    private static final long serialVersionUID = 8633299996744734593L;

    private int id;
    private double latitude;
    private double lontitude;
    private int img;
    private String des;

    public MarkerBean() {
    }

    public MarkerBean(int id, double latitude, double lontitude, int img, String des) {
        this.id = id;
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.img = img;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLontitude() {
        return lontitude;
    }

    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
