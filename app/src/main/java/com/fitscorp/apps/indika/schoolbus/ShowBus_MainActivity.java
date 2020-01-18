package com.fitscorp.apps.indika.schoolbus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.fitscorp.apps.indika.schoolbus.login.ContactActivity;
import com.fitscorp.apps.indika.schoolbus.login.MsgToDriver_Activity;
import com.fitscorp.apps.indika.schoolbus.login.UserRegister;
import com.fitscorp.apps.indika.schoolbus.model.BusMainListData;
import com.fitscorp.apps.indika.schoolbus.model.BusMainResponse;
import com.fitscorp.apps.indika.schoolbus.model.GPSMainData;
import com.fitscorp.apps.indika.schoolbus.model.GPSMainResponse;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBus_MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location locati;
    LatLng currentLocation;
    private Context mContext = null;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Marker mapMarker;
    double latitude; // Latitude
    double longitude; // Longitude
    List<LatLng> pointsNew = null;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 100 * 1 * 1; // 1 minute

    //  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 432;
    boolean isForstTime=true;
    boolean isPermissionGranted = false;
    LatLng sydney=null;
    double current_lat, current_longi, current_distance;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String str_lati;
    String str_longi,busid;
    TextView txt_bus_status;
    com.google.android.gms.maps.MapView mapView;
   // public static FirebaseAnalytics firebaseAnalytics;

    TextView txt_driver_name,txt_bus_number;
    PrefManager prefManager;
    String busContact;
    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        // FirebaseAnalytics Configurations...................
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        //Sets whether analytics collection is enabled for this app on this device.
//        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
//        firebaseAnalytics.setMinimumSessionDuration(10000);
//        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
//        firebaseAnalytics.setSessionTimeoutDuration(500);



        mapView = (com.google.android.gms.maps.MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }
        mContext= getApplication();
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        prefManager=new PrefManager(ShowBus_MainActivity.this);
        busid=prefManager.getBusRegistered();



        //  FirebaseAnalytics Adding
//        Bundle bWorkout = new Bundle();
//        bWorkout.putString("name","Home_Screen");
//        if(firebaseAnalytics!=null) {
//            firebaseAnalytics.logEvent("Home_Screen_opened", bWorkout);
//        }


        txt_bus_status=findViewById(R.id.txt_bus_status);

        txt_bus_number=findViewById(R.id.txt_bus_number);

        txt_driver_name=findViewById(R.id.txt_driver_name);
        txt_bus_number.setVisibility(View.INVISIBLE);
        txt_driver_name.setVisibility(View.INVISIBLE);
        txt_bus_number.setVisibility(View.INVISIBLE);

        prefManager=new PrefManager(ShowBus_MainActivity.this);

        txt_bus_status.setText("Bus not started");

     //   getBus();

        startTimer();
    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 3000, 30*1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            // Do something for lollipop and above versions
                            requestCameraPermission();
                        } else{
                            // do something for phones running an SDK before lollipop
                            getBusGPS();
                        }
                    }
                });
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Message",".......onDestroy");
        stoptimertask();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.mapView.onResume();



        getBusStatus();

    }


    public void getBus() {

        busid=prefManager.getBusRegistered();

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<BusMainResponse> call = apiService.isBusRegistered(busid);
        call.enqueue(new Callback<BusMainResponse>() {
            @Override
            public void onResponse(Call<BusMainResponse> call, Response<BusMainResponse> response) {

                if(response.isSuccessful()) {

                    List<BusMainListData> list= response.body().getData();

                    if(list.size()==0){
                        Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG).show();
                        txt_bus_number.setText("");
                    }else {
                        BusMainListData obj = list.get(0);
                        String bus_id = obj.getId();
                        String str_oner_name = obj.getOwnerName();
                        String str_oner_address = obj.getOwnerAddress();
                        String str_oner_phone = obj.getOwnerPhone();
                        String str_oner_email = obj.getOwnerEmail();
                        String str_driver_name = obj.getDriverName();
                        //   String nane=  obj.getDriverPhoto();
                        String str_driver_phone = obj.getDriverPhone();
                        String str_bus_number = obj.getBusNumber();
                        //  String nane=  obj.getBusPhoto();
                        String str_bus_number_photo = obj.getBusNumberPhoto();
                        String str_start_address = obj.getStartAddress();
                        String str_end_address = obj.getEndAddress();
                        String str_cross_cities = obj.getCrossCities();
                        String str_morning_time = obj.getTimeDesc();
                        //    prefManager.setBusId(result);
                        prefManager.createBusOwner(bus_id,str_oner_name, str_oner_address, str_oner_phone, str_oner_email,
                                str_driver_name, str_driver_phone, str_bus_number, str_bus_number_photo,
                                str_start_address, str_end_address, str_cross_cities, str_morning_time, str_morning_time);

                        txt_bus_number=findViewById(R.id.txt_bus_number);

                        txt_driver_name=findViewById(R.id.txt_driver_name);

                        String driverName="Driver Name : "+str_driver_name;
                        String busNumber="Bus Number : "+str_bus_number;
                        txt_bus_number.setText(busNumber);
                        txt_driver_name.setText(driverName);

                        busContact=str_driver_phone;
                    }
                }
            }
            @Override
            public void onFailure(Call<BusMainResponse> call, Throwable t) {
              //  progress.cancel();

                System.out.println("========t======"+t);
            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void editProfile(View view) {

//        Bundle bWorkout = new Bundle();
//        bWorkout.putString("name","Home_Screen");
//        if(firebaseAnalytics!=null) {
//            firebaseAnalytics.logEvent("editProfile_Clicked", bWorkout);
//        }

        Intent in=new Intent(this,UserRegister.class);
        startActivity(in);
        finish();
    }

    public void contactUs(View view) {
//        Bundle bWorkout = new Bundle();
//        bWorkout.putString("name","Home_Screen");
//        if(firebaseAnalytics!=null) {
//            firebaseAnalytics.logEvent("contactUs_Clicked", bWorkout);
//        }
     //   Intent in=new Intent(this,ContactActivity.class);
        Intent in=new Intent(this,ContactActivity.class);
        startActivity(in);

    }

    public void sendMessage(View view) {

//        Bundle bWorkout = new Bundle();
//        bWorkout.putString("name","Home_Screen");
//        if(firebaseAnalytics!=null) {
//            firebaseAnalytics.logEvent("sendMessage_Clicked", bWorkout);
//        }

        Intent in=new Intent(this,MsgToDriver_Activity.class);
        startActivity(in);
    }


    void updateCurrentLocation(String lat,String longi) {
        latitude= Double.parseDouble(lat);
        longitude= Double.parseDouble(longi);
//
            sydney = new LatLng(latitude, longitude);
//            CameraUpdate center = CameraUpdateFactory.newLatLng(sydney);
//            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
//            mMap.moveCamera(center);
//            mMap.animateCamera(zoom);
//            mapMarker.setPosition(sydney);
       // if(isForstTime) {
//            PrefManager pref = new PrefManager(ShowBus_MainActivity.this);
//            String slat = pref.getLat();
//            String slongi = pref.getLongi();
//
//            sydney = new LatLng(Double.valueOf(slat), Double.valueOf(slongi));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 15.0f));

            BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.new_bus_icon_sm);
            Bitmap b2 = bitmapdraw2.getBitmap();
            mapMarker = mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .icon(BitmapDescriptorFactory.fromBitmap(b2)));
         //   isForstTime=false;
       // }
        mapMarker.setPosition(sydney);



     //   isBusAlreadyRegistered();
    }



    public void getBusGPS() {
//        Bundle bWorkout = new Bundle();
//        bWorkout.putString("name","Home_Screen");
//        if(firebaseAnalytics!=null) {
//            firebaseAnalytics.logEvent("Refresh_Map", bWorkout);
//        }

        busid=prefManager.getBusRegistered();

       final ProgressDialog  progressDialog =new ProgressDialog(ShowBus_MainActivity.this);
        progressDialog.show();
        progressDialog.setMessage("GPS updating...");

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<GPSMainResponse> call = apiService.getBusLocations(busid);
        call.enqueue(new Callback<GPSMainResponse>() {
            @Override
            public void onResponse(Call<GPSMainResponse> call, Response<GPSMainResponse> response) {
                progressDialog.cancel();
                if(response.isSuccessful()) {
                  List<GPSMainData> data= response.body().getData();
                    str_lati=  data.get(0).getLat();
                    str_longi=  data.get(0).getLongi();


                    System.out.println("======================"+str_lati);
                    System.out.println("======================"+str_longi);
                    updateCurrentLocation(str_lati,str_longi);


                }
            }
            @Override
            public void onFailure(Call<GPSMainResponse> call, Throwable t) {
                progressDialog.cancel();

                System.out.println("========t======"+t);
            }
        });
    }

    public void getBusStatus() {
//        Bundle bWorkout = new Bundle();
//        bWorkout.putString("name","Home_Screen");
//        if(firebaseAnalytics!=null) {
//            firebaseAnalytics.logEvent("Bus_Status", bWorkout);
//        }
        busid=prefManager.getBusRegistered();

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.getBusStatus(busid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                 String busMsg="";
                 String busStatus=   response.body();
                 if(busStatus.equals("0")){

                 }
                 else  if(busStatus.equals("1")){
                     busMsg="Bus has started getting children";
                    }
                 else if(busStatus.equals("2")){
                     busMsg="Bus has started travelling";
                    }
                 else if(busStatus.equals("3")){
                     busMsg="Bus has reached to school";
                    }
                 else if(busStatus.equals("4")){
                     busMsg="Your child safety  entered to the school";
                 }
                    txt_bus_status.setText(busMsg);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("========t======"+t);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        // locationManager.removeUpdates(this);
    }

    private void requestCameraPermission() {

        // if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if(
                ActivityCompat.checkSelfPermission(ShowBus_MainActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(ShowBus_MainActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ShowBus_MainActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ShowBus_MainActivity.this,permissionsRequired[1])
                    ){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBus_MainActivity.this);
                builder.setTitle("Seva App Permissions");
                builder.setMessage("This app needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ShowBus_MainActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBus_MainActivity.this);
                builder.setTitle("Seva App Permissions");
                builder.setMessage("This app needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(ShowBus_MainActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            //   txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
          //  loginUserInServer();
            getBusGPS();

        }
    }

    public void RefreshGPS(View view) {

        getBusGPS();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
                    break;
            }
        }
        else if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(ShowBus_MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                getBusGPS();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if(allgranted){
                getBusGPS();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(ShowBus_MainActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ShowBus_MainActivity.this,permissionsRequired[1])
                    ){

                Toast.makeText(getBaseContext(),"Permissions Required",Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBus_MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ShowBus_MainActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        PrefManager pref=new PrefManager(ShowBus_MainActivity.this);
//        String slat= pref.getLat();
//        String slongi=pref.getLongi();
//
//        sydney = new LatLng(Double.valueOf(slat), Double.valueOf(slongi));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 15.0f));
//
//        BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.new_bus_icon_sm);
//        Bitmap b2 = bitmapdraw2.getBitmap();
//        mapMarker = mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .icon(BitmapDescriptorFactory.fromBitmap(b2)));
        // Add a marker in Sydney and move the camera



    }




    public void callToDriver(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        // intent.setData(Uri.parse("tel:0773771925"));
        intent.setData(Uri.parse("tel:"+busContact));
        startActivity(intent);

    }


//    @Override
//    public void onLocationChanged(Location location) {
//        locati=location;
//        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }



}
