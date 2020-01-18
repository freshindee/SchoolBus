package com.fitscorp.apps.indika.schoolbus.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fitscorp.apps.indika.schoolbus.*;
import com.fitscorp.apps.indika.schoolbus.firebase.NewMapKotlinActivity;
import com.fitscorp.apps.indika.schoolbus.model.ParentObj;
import com.fitscorp.apps.indika.schoolbus.model.ParentsDataModel;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentLoginActivity extends AppCompatActivity {

    EditText txt_bus_number;
    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        txt_bus_number=findViewById(R.id.txt_bus_number);

        pref=new PrefManager(this);




    }


    public void newRegister(View view) {

        Intent in=new Intent(this,UserRegister.class);
        startActivity(in);
        finish();
    }





    public void signInParent(String mobile_number) {
        final ProgressDialog progress=new ProgressDialog(ParentLoginActivity.this);
        progress.setMessage("Please wait...");
        progress.show();
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<ParentsDataModel> call = apiService.isParentRegistered(mobile_number);
        call.enqueue(new Callback<ParentsDataModel>() {
            @Override
            public void onResponse(Call<ParentsDataModel> call, Response<ParentsDataModel> response) {
                progress.cancel();
                if(response.isSuccessful()) {
                    PrefManager prefManager=new PrefManager(ParentLoginActivity.this);
                   List<ParentObj> list= response.body().getData();

                    if(list.size()==0){
                        Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG).show();
                        txt_bus_number.setText("");
                    }else{
                        ParentObj parent=list.get(0);
                        String id=parent.getId();
                        String name=parent.getName();
                        String phone=parent.getPhone();
                        String email= parent.getEmail();
                        String district=parent.getDistrict();
                        String gender= parent.getGender();
                        String age= parent.getBirthday();
                        prefManager.setParent(id,name,phone,email,parent.getCity(),district,age,gender);
                        prefManager.setParentAvailability(true);
                        Intent in = new Intent(ParentLoginActivity.this, MyChildrenList_Activity.class);
                        startActivity(in);
                    }

//                    if(list.size()==0){
//
//
//                    }else {
//                        BusMainListData obj = list.get(0);
//                        String bus_id = obj.getId();
//                        String str_oner_name = obj.getOwnerName();
//                        String str_oner_address = obj.getOwnerAddress();
//                        String str_oner_phone = obj.getOwnerPhone();
//                        String str_oner_email = obj.getOwnerEmail();
//                        String str_driver_name = obj.getDriverName();
//                        //   String nane=  obj.getDriverPhoto();
//                        String str_driver_phone = obj.getDriverPhone();
//                        String str_bus_number = obj.getBusNumber();
//                        //  String nane=  obj.getBusPhoto();
//                        String str_bus_number_photo = obj.getBusNumberPhoto();
//                        String str_start_address = obj.getStartAddress();
//                        String str_end_address = obj.getEndAddress();
//                        String str_cross_cities = obj.getCrossCities();
//                        String str_morning_time = obj.getTimeDesc();
//                        //    prefManager.setBusId(result);
//                        prefManager.createBusOwner(bus_id,str_oner_name, str_oner_address, str_oner_phone, str_oner_email,
//                                str_driver_name, str_driver_phone, str_bus_number, str_bus_number_photo,
//                                str_start_address, str_end_address, str_cross_cities, str_morning_time, str_morning_time);
//
//                        Intent in = new Intent(BusLoginActivity.this, MapsActivity.class);
//                        startActivity(in);
//                        finish();
                  //  }
                }
            }
            @Override
            public void onFailure(Call<ParentsDataModel> call, Throwable t) {
                progress.cancel();
                Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG).show();
                txt_bus_number.setText("");
                System.out.println("========t======"+t);
            }
        });
    }


    public void signIn(View view) {

        String phoneNumber= txt_bus_number.getText().toString();

        if(phoneNumber.length()>3){
            if(Utility.isInternetAvailable(this)) {
                signInParent(phoneNumber);
            }else{
                Toast.makeText(ParentLoginActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(ParentLoginActivity.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
        }

    }
}
