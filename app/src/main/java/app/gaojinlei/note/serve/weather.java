package app.gaojinlei.note.serve;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import app.gaojinlei.manager.Data;

/**
 * Created by 高金磊 on 2019/4/14.
 */

public class weather extends Service implements WeatherSearch.OnWeatherSearchListener{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //根据位置获取天气信息

        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST

        new Thread(){
            @Override
            public void run() {
                String city;
                WeatherSearchQuery mquery;
                WeatherSearch mweathersearch;
               while (true){
                   city=Data.getWeathercity();
                   if (city.equals("未知")){//快速刷新以即使获取城市信息
                       try {
                           sleep(100);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }else {
                       mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
                       mweathersearch=new WeatherSearch(getApplicationContext());
                       mweathersearch.setOnWeatherSearchListener(weather.this);
                       mweathersearch.setQuery(mquery);
                       mweathersearch.searchWeatherAsyn(); //异步搜索
                       try {
                           sleep(1000000);//降低刷新速度
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
            }
        }.start();


        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult , int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null&&weatherLiveResult.getLiveResult() != null) {
                String weather="";
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                weather+=weatherlive.getWeather();
                weather+=weatherlive.getTemperature()+"°";
                Data.setWeather(weather);
            }else {

            }
        }else {
            Log.e("天气","onWeatherLiveSearched:"+rCode);
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
