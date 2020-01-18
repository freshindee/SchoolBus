package com.fitscorp.apps.indika.schoolbus;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.fitscorp.apps.indika.schoolbus.login.LoginActivity;
import com.fitscorp.apps.indika.schoolbus.login.UserRegister;
import com.fitscorp.apps.indika.schoolbus.requests.AcceptCustomer;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location locati;LatLng latLng;
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

    boolean isPermissionGranted = false;

    double current_lat, current_longi, current_distance;

    //===============================
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    com.google.android.gms.maps.MapView mapView;
    ImageView img_menu; ImageView img_profile;
    RelativeLayout layout_daily_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        layout_daily_tip=findViewById(R.id.layout_daily_tip);
        layout_daily_tip.setVisibility(View.INVISIBLE);

        img_menu=findViewById(R.id.img_menu);
        img_profile=findViewById(R.id.img_profile);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent in=new Intent(MapsActivity.this, LocationPicker_Activity.class);
                Intent in=new Intent(MapsActivity.this, AcceptCustomer.class);
                startActivity(in);
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });


        mapView = (com.google.android.gms.maps.MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }

        mContext= getApplication();
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        this.mapView.onResume();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            requestCameraPermission();
        } else{
            // do something for phones running an SDK before lollipop
           getLocat();
        }

    }





    public void addUser() {


        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = apiService.addUser("indikaaaa","indikaaaa","indikaaaa");
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

    void getLocat() {
        Intent intent = new Intent(this, GPSService.class);
        startService(intent);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        String latitude = intent.getStringExtra(GPSService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(GPSService.EXTRA_LONGITUDE);

                        double lat = Double.parseDouble(latitude);
                        double lon = Double.parseDouble(longitude);

                        currentLocation = new LatLng(lat, lon);
                        Toast.makeText(MapsActivity.this, "got gps  " + latitude, Toast.LENGTH_SHORT).show();

                        if (latitude != null && longitude != null) {
                            //  txt_view.setText( latitude + "\n Longitude: " + longitude);
                            latLng = new LatLng(lat, lon);
                            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);
                            mapMarker.setPosition(latLng);
                        }
                    }
                }, new IntentFilter(GPSService.ACTION_LOCATION_BROADCAST)
        );
    }
    @Override
    protected void onStop() {
        super.onStop();

       // locationManager.removeUpdates(this);
    }

    private void requestCameraPermission() {

        // if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if(
                ActivityCompat.checkSelfPermission(MapsActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(MapsActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,permissionsRequired[1])
                    ){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("App Permissions");
                builder.setMessage("This app needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MapsActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
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
                ActivityCompat.requestPermissions(MapsActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            //   txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            getLocat();
        }
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
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                getLocat();
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
                getLocat();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,permissionsRequired[1])
                    ){

                Toast.makeText(getBaseContext(),"Permissions Required",Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MapsActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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

        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(6.867315, 79.890912);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 15.0f));

        BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.new_bus_icon_sm);
        Bitmap b2 = bitmapdraw2.getBitmap();
//        mapMarker = mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .icon(BitmapDescriptorFactory.fromBitmap(b2)));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  stoperviceAtivities();
    }

    private void stoperviceAtivities(){

        try {
            stopService(new Intent(this, GPSService.class));
        }catch(Exception e){
            e.printStackTrace();
        }
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
