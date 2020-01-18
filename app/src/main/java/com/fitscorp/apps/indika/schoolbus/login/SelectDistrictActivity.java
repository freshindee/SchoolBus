package com.fitscorp.apps.indika.schoolbus.login;



import android.os.Bundle;


import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SelectDistrictAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectDistrictActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SelectDistrictAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_district);

        List<String> dataList = new ArrayList<String>();

        dataList.add("Colombo");
        dataList.add("Kurunegala");
        dataList.add("Kandy");
        displayView(dataList);
    }


    void displayView( List<String> familyData){

        recyclerView = findViewById(R.id.recycler_familymemeber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new SelectDistrictAdapter(this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new SelectDistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {

                Ram.setDistrictName(object.toString());
                System.out.println("========================"+object.toString());
                finish();
            }
        } );
    }

}
