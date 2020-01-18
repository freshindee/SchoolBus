package com.fitscorp.apps.indika.schoolbus;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.fitscorp.apps.indika.schoolbus.login.AddMySchoolActivity;
import com.fitscorp.apps.indika.schoolbus.login.LoginActivity2_Activity;
import com.fitscorp.apps.indika.schoolbus.login.LoginActivity3_Activity;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SchoolListAdapter;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SelectDistrictAdapter;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListMainResponse;
import com.fitscorp.apps.indika.schoolbus.requests.SendBusRequest;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
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

public class ShowSchools extends AppCompatActivity {
    RecyclerView recyclerView;
    SchoolListAdapter adapter;
    PrefManager pref;
    ProgressDialog progressDialog;String childID,districtID,schoolName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);


        pref=new PrefManager(this);
        childID =  pref.getStudentId();
        //childID
        List dataList=new ArrayList();
        dataList= Ram.getSchoolDataList();
        ImageView btn_add_new_bus=findViewById(R.id.btn_add_new_bus);
        btn_add_new_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(ShowSchools.this,AddMySchoolActivity.class);
                startActivity(in);
            }
        });

        getSchoolList();


    }

    public void getSchoolList() {
        progressDialog=new ProgressDialog(ShowSchools.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<SchoolListMainResponse> call = apiService.getSchoolList("Colombo");

        call.enqueue(new Callback<SchoolListMainResponse>() {
            @Override
            public void onResponse(Call<SchoolListMainResponse> call, Response<SchoolListMainResponse> response) {
                progressDialog.cancel();

                if(response.isSuccessful()) {

                    Gson gson1 = new Gson();


                    List<SchoolListData> dataList= response.body().getData();
                    Ram.setSchoolDataList(dataList);
                    String jsonCars = gson1.toJson(response.body());
                    System.out.println("=======jsonCars======"+jsonCars);
                    displayView(dataList);
                }
            }
            @Override
            public void onFailure(Call<SchoolListMainResponse> call, Throwable t) {
                progressDialog.cancel();

                System.out.println("========t======"+t);
            }
        });
    }

    void displayView( List<SchoolListData> familyData){

        recyclerView = findViewById(R.id.recycler_school_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new SchoolListAdapter(ShowSchools.this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new SelectDistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {
                SchoolListData schoolObj=(SchoolListData)object;
                String schoolID= schoolObj.getId();
                String schoolName=schoolObj.getName();
                Intent in=new Intent(ShowSchools.this,SendBusRequest.class);
                in.putExtra("schoolID", schoolID);
                in.putExtra("childID", childID);

                in.putExtra("schoolName", schoolName);
                startActivity(in);
                PrefManager pref=new PrefManager(ShowSchools.this);
                pref.setLati(schoolObj.getLat());
                pref.setLongi(schoolObj.getLongi());


            }
        } );
    }

}
