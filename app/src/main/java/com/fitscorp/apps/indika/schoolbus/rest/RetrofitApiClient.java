package com.fitscorp.apps.indika.schoolbus.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {

    public static final String BASE_URL = "http://srilankatraveldeals.com/kuruvi/api/public/";
    public static final String IMAGE_URL = "http://srilankatraveldeals.com/kuruvi/api/public/images/";

    public static final String SUB_IMAGE_URL = "http://srilankatraveldeals.com/kuruvi/api/public/images/location_sub/";
    public static final String TRADINGS_IMAGE_URL = "http://srilankatraveldeals.com/kuruvi/api/public/images/tradings/";
    public static final String HOTEL_IMAGE_URL = "http://srilankatraveldeals.com/kuruvi/api/public/images/hotels/";
    public static final String MAP_IMAGE_URL = "http://srilankatraveldeals.com/kuruvi/api/public/images/map/";

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }
}
