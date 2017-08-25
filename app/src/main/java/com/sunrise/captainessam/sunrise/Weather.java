package com.sunrise.captainessam.sunrise;

/**
 * Created by Captain Essam on 21/08/2017.
 */

public class Weather {
    private  String speed;
    private String deg;
    private String Country;
    private String City;
    private String desc;
    private  String DN;
    private String Cloud;

    public String getCloud() {
        return Cloud;
    }

    public void setCloud(String cloud) {
        Cloud = cloud;
    }

    public String getDN() {
        return DN;
    }

    public void setDN(String DN) {
        this.DN = DN;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHumidtiy() {
        return Humidtiy;
    }

    public void setHumidtiy(String humidtiy) {
        Humidtiy = humidtiy;
    }


    private String Humidtiy;



    private int Sunrise;
    private int Sunset;

    public int getSunrise() {
        return Sunrise;
    }

    public void setSunrise(int sunrise) {
        Sunrise = sunrise;
    }

    public int getSunset() {
        return Sunset;
    }

    public void setSunset(int sunset) {
        Sunset = sunset;
    }

    public int getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        LastUpdate = lastUpdate;
    }

    private int LastUpdate;
}
