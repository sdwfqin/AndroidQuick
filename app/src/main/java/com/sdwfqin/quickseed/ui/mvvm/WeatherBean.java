package com.sdwfqin.quickseed.ui.mvvm;

/**
 * 天气bean
 * <p>
 *
 * @author 张钦
 * @date 2020/4/14
 */
public class WeatherBean {

    private Integer errcode;
    private String errmsg;
    private String cityid;
    private String city;
    private String update_time;
    private String wea;
    private String wea_img;
    private String tem;
    private String tem_day;
    private String tem_night;
    private String win;
    private String win_speed;
    private String win_meter;
    private String air;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getWea() {
        return wea;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public String getWea_img() {
        return wea_img;
    }

    public void setWea_img(String wea_img) {
        this.wea_img = wea_img;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getTem_day() {
        return tem_day;
    }

    public void setTem_day(String tem_day) {
        this.tem_day = tem_day;
    }

    public String getTem_night() {
        return tem_night;
    }

    public void setTem_night(String tem_night) {
        this.tem_night = tem_night;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getWin_speed() {
        return win_speed;
    }

    public void setWin_speed(String win_speed) {
        this.win_speed = win_speed;
    }

    public String getWin_meter() {
        return win_meter;
    }

    public void setWin_meter(String win_meter) {
        this.win_meter = win_meter;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", cityid='" + cityid + '\'' +
                ", city='" + city + '\'' +
                ", update_time='" + update_time + '\'' +
                ", wea='" + wea + '\'' +
                ", wea_img='" + wea_img + '\'' +
                ", tem='" + tem + '\'' +
                ", tem_day='" + tem_day + '\'' +
                ", tem_night='" + tem_night + '\'' +
                ", win='" + win + '\'' +
                ", win_speed='" + win_speed + '\'' +
                ", win_meter='" + win_meter + '\'' +
                ", air='" + air + '\'' +
                '}';
    }
}
