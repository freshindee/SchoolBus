package com.fitscorp.apps.indika.schoolbus.login;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.ShowBus_MainActivity;
import com.fitscorp.apps.indika.schoolbus.ShowSchools;
import com.fitscorp.apps.indika.schoolbus.Utility;
import com.fitscorp.apps.indika.schoolbus.login.adapter.ChildListAdapter;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SchoolListAdapter;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SelectDistrictAdapter;
import com.fitscorp.apps.indika.schoolbus.model.ChildMainData;
import com.fitscorp.apps.indika.schoolbus.model.ChildMainResponse;
import com.fitscorp.apps.indika.schoolbus.model.ParentObj;
import com.fitscorp.apps.indika.schoolbus.model.ParentsDataModel;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListMainResponse;
import com.fitscorp.apps.indika.schoolbus.requests.SendBusRequest;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.gson.Gson;
import com.mikhaellopez.lazydatepicker.LazyDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class MyChildrenList_Activity extends AppCompatActivity {
    List<ChildMainData> dataList;
    ApiInterface apiService;
    RecyclerView recyclerView;
    ChildListAdapter adapter;
   PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children_list_);

        ImageView btn_add_new_bus=findViewById(R.id.btn_add_new_bus);
        btn_add_new_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((dataList!=null) && (dataList.size()>0)){
                    Intent in=new Intent(MyChildrenList_Activity.this,AddNewChild.class);
                    startActivity(in);
                    finish();
                    Toast.makeText(MyChildrenList_Activity.this,"Currently you can not add more children",Toast.LENGTH_LONG).show();
                }else{
                    Intent in=new Intent(MyChildrenList_Activity.this,AddNewChild.class);
                    startActivity(in);
                    finish();
                }

            }
        });

        pref=new PrefManager(MyChildrenList_Activity.this);

        getSchoolListByDistrict();

    }

    public void getSchoolListByDistrict() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String parent_id=pref.getParent().get("parent_id");

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<ChildMainResponse> call = apiService.getChildByParentId(parent_id);
        call.enqueue(new Callback<ChildMainResponse>() {
            @Override
            public void onResponse(Call<ChildMainResponse> call, Response<ChildMainResponse> response) {
                progressDialog.cancel();

                if(response.isSuccessful()) {

                     dataList= response.body().getData();

                    if((dataList!=null) && (dataList.size()>0)){

                          displayView(dataList);
                    }else{
                        Intent in=new Intent(MyChildrenList_Activity.this,AddNewChild.class);
                        startActivity(in);
                        finish();
                    }


                }
            }
            @Override
            public void onFailure(Call<ChildMainResponse> call, Throwable t) {
                progressDialog.cancel();

                System.out.println("========t======"+t);
            }
        });
    }

    void displayView( List<ChildMainData> familyData){

        recyclerView = findViewById(R.id.recycler_familymemeber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new ChildListAdapter(MyChildrenList_Activity.this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new SelectDistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {

              ChildMainData schoolObj=(ChildMainData)object;
              Intent in = new Intent(MyChildrenList_Activity.this, ShowSchools.class);
              pref.setStudentId(schoolObj.getId());
              pref.setStudentAvailability(true);
              startActivity(in);
              finish();

              //  pref.setLati(schoolObj.getLat());
              //  pref.setLongi(schoolObj.getLongi());


            }
        } );
    }




}
