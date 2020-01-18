package com.fitscorp.apps.indika.schoolbus.requests;

import android.os.Bundle;


import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.login.adapter.AcceptCustomerAdapter;
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

public class AcceptCustomer extends AppCompatActivity {

    RecyclerView recyclerView;
    AcceptCustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_customer);


        List dataList=new ArrayList();
        dataList= Ram.getSchoolDataList();


        getSchoolList();

    }


    public void getSchoolList() {
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<SchoolListMainResponse> call = apiService.getMyBusRequests("1");
        call.enqueue(new Callback<SchoolListMainResponse>() {
            @Override
            public void onResponse(Call<SchoolListMainResponse> call, Response<SchoolListMainResponse> response) {

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


                System.out.println("========t======"+t);
            }
        });
    }



    void displayView( List<SchoolListData> familyData){

        recyclerView = findViewById(R.id.recycler_familymemeber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new AcceptCustomerAdapter(this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


//        adapter.setOnItemClickListener(new SelectDistrictAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Object object) {
//                SchoolListData schoolObj=(SchoolListData)object;
//                String schoolID= schoolObj.getId();
//                Intent in=new Intent(AcceptCustomer.this,SendBusRequest.class);
//                in.putExtra("schoolID", schoolID);
//                startActivity(in);
////                Ram.setDistrictName(object.toString());
////                System.out.println("========================"+object.toString());
//
//            }
//        } );
    }


}
