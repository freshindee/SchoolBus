package com.fitscorp.apps.indika.schoolbus.requests;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.ShowBus_MainActivity;
import com.fitscorp.apps.indika.schoolbus.firebase.NewMapKotlinActivity;
import com.fitscorp.apps.indika.schoolbus.login.UserRegister;
import com.fitscorp.apps.indika.schoolbus.login.VerificationPendingActivity;
import com.fitscorp.apps.indika.schoolbus.model.BusListMainResponse;
import com.fitscorp.apps.indika.schoolbus.model.SchoolBusData;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.gson.Gson;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusDetailsActivity extends AppCompatActivity {
    PrefManager pref;
    String busID,schoolID,parentID;
    SchoolBusData busObject;

    String schoolName,imageURL,childID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        pref=new PrefManager(this);

        childID = getIntent().getStringExtra("childID");
        busID = getIntent().getStringExtra("busID");
        schoolID = getIntent().getStringExtra("schoolID");
        schoolName = getIntent().getStringExtra("schoolName");
        busObject=  Ram.getBusObject();

        ImageView img_bus_photo=findViewById(R.id.img_bus_photo);
        TextView txt_bus_number=findViewById(R.id.txt_bus_number);
        TextView txt_driver_name=findViewById(R.id.txt_driver_name);
        TextView txt_start_location=findViewById(R.id.txt_start_location);
        TextView txt_cross_cities=findViewById(R.id.txt_cross_cities);

        TextView txt_destination=findViewById(R.id.txt_destination);
        TextView txt_time=findViewById(R.id.txt_time);

        txt_bus_number.setText("Bus Number : "+busObject.getBusNumber());
        txt_driver_name.setText("Driver Name : "+busObject.getDriverName());
        txt_start_location.setText("From : "+busObject.getStartAddress());
        txt_cross_cities.setText("Cross Cities : "+busObject.getCrossCities());
        txt_destination.setText("Destination : "+schoolName);
        txt_time.setText("Time : "+busObject.getTimeDesc());


        imageURL=RetrofitApiClient.IMAGE_URL+"bus/"+busObject.getBusPhoto();
        // http://srilankatraveldeals.com/kuruvi/api/public/images/bus/bus_1551018394


        int imageSize=120;
        RequestOptions requestOptions = new RequestOptions().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(BusDetailsActivity.this).load(imageURL)
                .apply(requestOptions)
                .into(img_bus_photo);



    }

    public void assignBusToParent(String busid,String parentID,String school_id) {

      final ProgressDialog progressDialog=new ProgressDialog(BusDetailsActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.assignBusToParent(busid,parentID,school_id,childID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.cancel();
                if(response.isSuccessful()) {

                    if(response.body().equals("1")){

                        pref.setBusRegistered(busID);
                        Intent in=new Intent(BusDetailsActivity.this, NewMapKotlinActivity.class);
                        startActivity(in);
                        finish();

                    }else{
                        Log.d("API Response", "Assigning Fail");
                    }


                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressDialog.cancel();

                pref.setBusRegistered(busID);
                Intent in=new Intent(BusDetailsActivity.this, NewMapKotlinActivity.class);
                startActivity(in);
                finish();

                System.out.println("========t======"+t);
            }
        });
    }


    public void sendBusRequest(View view) {

        parentID=  pref.getParent().get("parent_id");
        busID= busObject.getId();
        pref.setBusAvailability(true);

        assignBusToParent(busID,parentID,schoolID);

//        pref.setBusRegistered(busID);
//        Intent in=new Intent(BusDetailsActivity.this, NewMapKotlinActivity.class);
//        startActivity(in);
//        finish();
    }
}
