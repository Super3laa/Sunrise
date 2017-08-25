package com.sunrise.captainessam.sunrise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Captain Essam on 22/08/2017.
 */

public class JSONWeather {
    public static Weather getWeather(String data)  {
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(data.toString());
            JSONObject mainObject = jsonObject.getJSONObject("main");
            weather.setDeg(mainObject.getString("temp").toString());
            weather.setHumidtiy(mainObject.getString("humidity".toString()));

            JSONObject windObject = jsonObject.getJSONObject("wind");
            weather.setSpeed(windObject.get("speed").toString());

            JSONObject cloudObject = jsonObject.getJSONObject("clouds");
            weather.setCloud(cloudObject.get("all").toString());

            JSONObject sysObject = jsonObject.getJSONObject("sys");
            weather.setSunrise(sysObject.getInt("sunrise"));
            weather.setSunset(sysObject.getInt("sunset"));

            weather.setCountry(sysObject.getString("country"));
            weather.setCity(jsonObject.getString("name"));
            weather.setLastUpdate(jsonObject.getInt("dt"));

            JSONArray weatherArr = jsonObject.getJSONArray("weather");
            JSONObject jsonweather = weatherArr.getJSONObject(0);
            weather.setDesc(jsonweather.getString("description").toString());
            weather.setDN(jsonweather.getString("icon").toString());
            return  weather;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }



    }
}