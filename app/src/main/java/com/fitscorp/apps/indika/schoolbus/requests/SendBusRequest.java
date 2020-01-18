package com.fitscorp.apps.indika.schoolbus.requests;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;


import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.ShowSchools;
import com.fitscorp.apps.indika.schoolbus.login.SelectDistrictActivity;
import com.fitscorp.apps.indika.schoolbus.login.UserRegister;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SelectDistrictAdapter;
import com.fitscorp.apps.indika.schoolbus.model.BusListMainResponse;
import com.fitscorp.apps.indika.schoolbus.model.SchoolBusData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListMainResponse;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendBusRequest extends AppCompatActivity {
    RecyclerView recyclerView;
    SendBusRequestAdapter adapter;
    String  schoolID,schoolName,childID;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_bus_request);

        List dataList=new ArrayList();

        childID = getIntent().getStringExtra("childID");
        schoolID = getIntent().getStringExtra("schoolID");
        schoolName = getIntent().getStringExtra("schoolName");

        getBusList(schoolID);



    }

    public void getBusList(String schoolID) {
        progressDialog=new ProgressDialog(SendBusRequest.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<BusListMainResponse> call = apiService.getBusesForSchoolList(schoolID);
        call.enqueue(new Callback<BusListMainResponse>() {
            @Override
            public void onResponse(Call<BusListMainResponse> call, Response<BusListMainResponse> response) {
                progressDialog.cancel();
                if(response.isSuccessful()) {

                    Gson gson1 = new Gson();


                    List<SchoolBusData> dataList= response.body().getData();
                  //  Ram.setSchoolDataList(dataList);
                    String jsonCars = gson1.toJson(response.body());
                    System.out.println("=======jsonCars======"+jsonCars);
                    displayView(dataList);
                }
            }
            @Override
            public void onFailure(Call<BusListMainResponse> call, Throwable t) {
                progressDialog.cancel();

                System.out.println("========t======"+t);
            }
        });
    }



    void displayView( List<SchoolBusData> familyData){

        recyclerView = findViewById(R.id.recycler_familymemeber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new SendBusRequestAdapter(this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new SelectDistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {

                SchoolBusData schoolObj=(SchoolBusData)object;
                String busId= schoolObj.getId();
                Ram.setBusObject(schoolObj);
                Intent in=new Intent(SendBusRequest.this,BusDetailsActivity.class);

                in.putExtra("childID", childID);
                in.putExtra("schoolName", schoolName);
                in.putExtra("schoolID", schoolID);
                in.putExtra("busID", busId);

                startActivity(in);

            }
        } );
    }

}
