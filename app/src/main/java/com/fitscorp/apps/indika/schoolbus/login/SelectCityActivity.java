package com.fitscorp.apps.indika.schoolbus.login;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.login.adapter.CityAdapter;
import com.fitscorp.apps.indika.schoolbus.model.City;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
    androidx.recyclerview.widget.RecyclerView recyclerView;
    CityAdapter adapter;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        editText= findViewById(R.id.edt_search_value);
        getCities();
    }


    void displayView(List<City> familyData) {

        recyclerView = findViewById(R.id.main_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new CityAdapter(this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filterData(charSequence.toString());

                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {
                City city= (City) object;

                PrefManager pref=new PrefManager(SelectCityActivity.this);
                pref.setCity(city.getName());
               finish();
            }
        } );
    }


    public void getCities() {

        PrefManager pref=new PrefManager(SelectCityActivity.this);
        String user_id= pref.getParent().get("parent_id");
        final ProgressDialog progress=new ProgressDialog(SelectCityActivity.this);
        progress.setMessage("Please wait...");
        progress.show();

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<List<City>> call = apiService.getCities("");
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                progress.cancel();
                if(response.isSuccessful()) {

                  //  showAlert_Add(ContactActivity.this, "Thank You . We will contact you soon");
                    List<City> cityList=   response.body();
                    displayView(cityList);
                    System.out.println("==============");
                }
            }
            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                progress.cancel();
                Toast.makeText(getApplicationContext(), "Request Fail. Try again later", Toast.LENGTH_LONG).show();

                System.out.println("========t======"+t);
            }
        });
    }

}
