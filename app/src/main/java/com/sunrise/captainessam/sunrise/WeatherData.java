package com.sunrise.captainessam.sunrise;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Captain Essam on 22/08/2017.
 */

public class WeatherData {
    public String getWeatherdata(URL url){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String sb  = null;
        try {

            urlConnection=(HttpURLConnection)url.openConnection();
            if (urlConnection.getResponseCode() == 200) {
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                sb = stringBuffer.toString();
            }
            return  sb;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

}
