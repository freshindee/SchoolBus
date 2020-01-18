package com.fitscorp.apps.indika.schoolbus;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by appdev on 9/15/2017.
 */

public class GPSService extends Service implements LocationListener {
    private static final String TAG = GPSService.class.getSimpleName();
    public static final String ACTION_LOCATION_BROADCAST = GPSService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    Location firstLocation=null;
    private Context mContext=null;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude
    List<LatLng> pointsNew = null;
    String str_latitude, str_longitude;
      private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
      private static final long MIN_TIME_BW_UPDATES = 1000 * 4 * 1;

    //  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;// 1 minute
    // Declaring a Location Manager
    protected LocationManager locationManager;

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

//    public LocationMonitoringService(Context context) {
//        this.mContext = context;
//        getLocation();
//        pointsNew = new ArrayList<LatLng>();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUsingGPS();

    }
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager=null;
        }
    }

    public void updateCurrentLocation(){

            Service_userLogout task = new Service_userLogout();
            task.execute(new String[]{""});

    }

    private class Service_userLogout extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            makePostRequest5();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }


    }

    public void addBusLocation() {
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = apiService.addBusLocation("6.915952","79.859775","1");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.isSuccessful()) {

                    Gson gson1 = new Gson();
                    String jsonCars = gson1.toJson(response.body());
                    System.out.println("=======jsonCars======"+jsonCars);
                    //readFavoriteDoctors(jsonCars);
                    // AppHandler.writeOfflineData(MainActivity.this, AppConfig.OFFLINE_FILE_FAVOURITE, jsonCars);

                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {


                System.out.println("========t======"+t);
            }
        });
    }


    private void makePostRequest5() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://findaworker.000webhostapp.com/updateCurrentLocation.php");
       // HttpPost httpPost = new HttpPost("http://taprobanes.com/schoolbus/updateCurrentLocation.php");
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        nameValuePair.add(new BasicNameValuePair("str_latitude", str_latitude));
        nameValuePair.add(new BasicNameValuePair("str_longitude",str_longitude));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
       int responseCode=  response.getStatusLine().getStatusCode();
        if(responseCode==200) {
       //succssssssssss
            System.out.println("============succssssssssss==================");
        }
        else{
            // HTTP Response not 200
            System.out.println("============fail==================");
        }


    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    public Location getLocation() {
        try {
            mContext= getApplication();
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("GPS Dnabled", "GPS Dnabled");
                // No network provider is enabled

            } else {
                this.canGetLocation = true;

                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        pointsNew = new ArrayList<LatLng>();

                        // setUpMap();

                        //   turnGPSOn();


                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            //  return true;
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location == null){
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        1000,
                                        0, this);
                            }
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("GPS Enabled", "GPS Running...........");
                                sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                            }
                        }
                    }
                }

                else if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        }
                    }
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // this.mContext = context;
        getLocation();
      //  System.out.println("============onStartCommand==================");
        pointsNew = new ArrayList<LatLng>();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("============onLocationChanged==================");
        if (location != null) {
        //    Log.d(TAG, "== location != null");
            Log.d("GPS Enabled", "========================onLocationChanged========="+location.getLatitude());
            //Send result to activities
            str_latitude=String.valueOf(location.getLatitude());
            str_longitude=String.valueOf(location.getLongitude());
            if(firstLocation!=null){
                float distanceInMeters = firstLocation.distanceTo(location);
                if(distanceInMeters >10){
                    System.out.println("============distanceInMeters=================="+distanceInMeters);
                    addBusLocation();
                    sendMessageToUI(str_latitude, str_longitude);
                }

            }
            firstLocation=location;
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
