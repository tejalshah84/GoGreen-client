package com.example.amanleenpuri.gogreen.ui;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import util.VolleyAppController;
import ws.remote.GreenRESTInterface;

/**
 * Created by amanleenpuri on 5/1/16.
 */
public class GoGreenApplication extends Application {



    public static final String BASE_URL = "http://192.168.0.6:8080/GoGreenServer/";
   // public static final String BASE_URL = "http://172.29.92.27:8080/GoGreenServer/";

    //public static final String BASE_URL = "http://10.1.10.15:8080/GoGreenServer/";

   // public static final String BASE_URL = "http://172.29.93.176:8080/GoGreenServer/";
//    public static final String BASE_URL = "http://172.29.93.176:8080/GoGreenServer/";
  //  public static final String BASE_URL = "http://10.1.10.15:8080/GoGreenServer/";
    private Retrofit mRetrofitService;
    private GreenRESTInterface mGoGreenApiService;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // Trailing slash is needed
        mRetrofitService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        mGoGreenApiService = mRetrofitService.create(GreenRESTInterface.class);
        VolleyAppController.onApplicationCreate(this);
    }


    public GreenRESTInterface getGoGreenApiService() {
        return mGoGreenApiService;
    }

}
