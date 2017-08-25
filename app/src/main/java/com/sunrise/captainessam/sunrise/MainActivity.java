package com.sunrise.captainessam.sunrise;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener{
    private TextView speed, deg, desc, Humidty, Sunrise, city,country;
    Double lat,lan;
    SharedPreferences sharedPreferences;
    Weather weather = new Weather();
    LocationManager locationManager;
    public String City,Country;
    String provider;
    private ImageView iv;
    RelativeLayout RL;
    private ImageButton id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Save Data
        sharedPreferences = getSharedPreferences("weather",Context.MODE_PRIVATE);
        country=(TextView)findViewById(R.id.textView5);
        speed = (TextView) findViewById(R.id.Wind);
        deg = (TextView) findViewById(R.id.deg);
        desc = (TextView) findViewById(R.id.desc);
        Humidty = (TextView) findViewById(R.id.Humidtiy);
        Sunrise = (TextView) findViewById(R.id.Sunrise);
        iv = (ImageView) findViewById(R.id.imageView);
        city = (TextView) findViewById(R.id.textView4);
        RL = (RelativeLayout)findViewById(R.id.BG);
        id = (ImageButton)findViewById(R.id.imageButton) ;
        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,About.class);
                startActivity(intent);

            }
        });
        GetData();
        if (weather.getDN().indexOf("d")>=0){
            this.getWindow().setStatusBarColor(Color.parseColor("#D42E2E"));
            RL.setBackgroundColor(Color.parseColor("#EE534F"));
            iv.setImageResource(R.drawable.sun);
        }else{
            this.getWindow().setStatusBarColor(Color.parseColor("#2E3BA1"));
            RL .setBackgroundColor(Color.parseColor("#4C5ABB"));
            iv.setImageResource(R.drawable.moon);
        }
        //Fetching lat,lan
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.SYSTEM_ALERT_WINDOW},0);
        }

        provider = locationManager.getBestProvider(new Criteria(),false);
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);
        if (location == null){
            Log.e("TAG","No Location found");
        }
        //Refresh
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        //Toast.makeText(MainActivity.this,"Updating Weather",Toast.LENGTH_SHORT).show();
                        if (AppStatus.getInstance(MainActivity.this).isOnline()) {

                            URL url = null;
                            try {
                                url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lan + "&appid=156fe3779f3aa7206a0921ee9aca9784&units=metric");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            renderweatherdata(url);
                            if (weather.getDN().indexOf("d")>=0){
                                MainActivity.this.getWindow().setStatusBarColor(Color.parseColor("#D42E2E"));
                                RL.setBackgroundColor(Color.parseColor("#EE534F"));
                                iv.setImageResource(R.drawable.sun);
                            }else{
                                MainActivity.this.getWindow().setStatusBarColor(Color.parseColor("#2E3BA1"));
                                RL .setBackgroundColor(Color.parseColor("#4C5ABB"));
                                iv.setImageResource(R.drawable.moon);
                            }
                        } else {

                        }

                    }
                },3000);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,400,1,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (AppStatus.getInstance(this).isOnline()) {
            if (location != null) {
                URL url = null;
                try {
                    lan = location.getLongitude();
                    lat = location.getLatitude();
                    Geocoder geocoder;
                    List<Address> Addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());
                    Addresses = geocoder.getFromLocation(lat, lan, 1);
                    Country = Addresses.get(0).getCountryName();
                    City = Addresses.get(0).getLocality()+", "+Addresses.get(0).getAdminArea();
                    country.setText(Addresses.get(0).getCountryName());
                    city.setText(Addresses.get(0).getLocality()+", "+Addresses.get(0).getAdminArea());
                    url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=156fe3779f3aa7206a0921ee9aca9784&units=metric");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                renderweatherdata(url);
            }

        } else {
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT);

        }




    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public  void SetData(){
        SharedPreferences.Editor weatherData = sharedPreferences.edit();
        weatherData.clear();
        weatherData.putString("Deg",(" "+(int) Math.ceil( Double.parseDouble(weather.getDeg()))+"°"));
        weatherData.putString("Desc",(" "+weather.getDesc().toString()));
        weatherData.putString("Speed",(weather.getSpeed().toString()+"m/s"));
        weatherData.putString("Humidity",(weather.getHumidtiy().toString()+"%"));
        weatherData.putString("Cloud",weather.getCloud().toString()+"%");
        weatherData.putString("DN",weather.getDN().toString());
        weatherData.putString("City",City);
        weatherData.putString("Country",Country);
        weatherData.commit();
    }
    public  void GetData(){
        deg.setText(sharedPreferences.getString("Deg","--"));
        desc.setText(sharedPreferences.getString("Desc","--"));
        speed.setText(sharedPreferences.getString("Speed","--"));
        Humidty.setText(sharedPreferences.getString("Humidity","--"));
        Sunrise.setText(sharedPreferences.getString("Cloud","--"));
        city.setText(sharedPreferences.getString("City","city"));
        country.setText(sharedPreferences.getString("Country","Country"));
        weather.setDN(sharedPreferences.getString("DN","01n").toString());

    }



    public void renderweatherdata(URL url) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(url);

    }

    public class WeatherTask extends AsyncTask<URL, Void, Weather> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            deg.setText(" "+(int) Math.ceil( Double.parseDouble(weather.getDeg()))+"°");
            desc.setText(weather.getDesc().toString());
            speed.setText(weather.getSpeed().toString()+"m/s");
            Humidty.setText(weather.getHumidtiy().toString()+"%");
            Sunrise.setText(weather.getCloud().toString()+"%");

            SetData();
            pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.setMessage("Getting weather data");
            pd.show();

        }

        @Override
        protected Weather doInBackground(URL... params) {

            String data = ((new WeatherData().getWeatherdata(params[0])));
            weather = JSONWeather.getWeather(data);
            return weather;
        }

    }

}
