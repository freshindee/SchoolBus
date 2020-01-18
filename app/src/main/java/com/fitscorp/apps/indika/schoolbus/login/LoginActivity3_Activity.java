package com.fitscorp.apps.indika.schoolbus.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fitscorp.apps.indika.schoolbus.MapsActivity;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.rest.AndroidMultiPartEntity;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity3_Activity extends AppCompatActivity {
    String str_oner_name,str_oner_phone,str_oner_email,str_oner_address,str_bus_number,str_driver_name,str_driver_phone;

    EditText txt_start_address,txt_end_address,txt_cross_cities,txt_morning_time,txt_evening_time;
    String str_start_address,str_end_address,str_cross_cities,str_morning_time,str_evening_time,URL;
    String str_bus_number_photo;

    PrefManager prefManager;

    File imageFile;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity3_);

        prefManager=new PrefManager(LoginActivity3_Activity.this);

        imageFile=Ram.getBusImage();

        str_oner_name = getIntent().getStringExtra("str_oner_name");
        str_oner_phone = getIntent().getStringExtra("str_oner_phone");
        str_oner_email = getIntent().getStringExtra("str_oner_email");
        str_oner_address = getIntent().getStringExtra("str_oner_address");

        str_bus_number = getIntent().getStringExtra("str_bus_number");
        str_driver_name = getIntent().getStringExtra("str_driver_name");
        str_driver_phone = getIntent().getStringExtra("str_driver_phone");


        txt_start_address=findViewById(R.id.txt_start_address);
        txt_end_address=findViewById(R.id.txt_end_address);
        txt_cross_cities=findViewById(R.id.txt_cross_cities);
        txt_morning_time=findViewById(R.id.txt_morning_time);
        txt_evening_time=findViewById(R.id.txt_evening_time);
        prgDialog=new ProgressDialog(LoginActivity3_Activity.this);
    }

    public void clickDriverRegister(View view) {

        str_start_address= txt_start_address.getText().toString();
        str_end_address= txt_end_address.getText().toString();
        str_cross_cities= txt_cross_cities.getText().toString();
        str_morning_time= txt_morning_time.getText().toString();
        str_evening_time= txt_evening_time.getText().toString();

        new UploadImage().execute();

      //  saveBus();
    }


    public void saveBus() {



        final ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.show();
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);



        Call<Object> call = apiService.saveBus(str_oner_name,str_oner_address,str_oner_phone,str_oner_email,str_driver_name,str_driver_phone,str_bus_number,str_bus_number_photo,
                str_start_address,str_end_address,str_cross_cities,str_morning_time,str_morning_time);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                progress.cancel();
                if(response.isSuccessful()) {

//                    prefManager.createBusOwner(str_oner_name,str_oner_address,str_oner_phone,str_oner_email,str_driver_name,str_driver_phone,str_bus_number,str_bus_number_photo,
//                            str_start_address,str_end_address,str_cross_cities,str_morning_time,str_morning_time);

                    Intent in=new Intent(LoginActivity3_Activity.this,MapsActivity.class);
                    startActivity(in);
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progress.cancel();

                System.out.println("========t======"+t);
            }
        });
    }


    private class UploadImage extends AsyncTask<Void, Integer, String> {
        String responseString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            URL="http://srilankatraveldeals.com/kuruvi/api/public/";
          //  http://srilankatraveldeals.com/kuruvi/api/public/images/bus/bus_1548183872
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL+"savebus");

            // httppost.addHeader("Authorization" , appToken);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //   publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                entity.addPart("owner_name",new StringBody(str_oner_name));
                entity.addPart("owner_address",new StringBody(str_oner_address));
                entity.addPart("owner_phone",new StringBody(str_oner_phone));
                entity.addPart("owner_email",new StringBody(str_oner_email));
                entity.addPart("driver_name",new StringBody(str_driver_name));
                entity.addPart("driver_photo",new StringBody("No"));
                entity.addPart("driver_phone",new StringBody(str_driver_phone));
                entity.addPart("bus_number",new StringBody(str_bus_number));
                entity.addPart("bus_number_photo",new StringBody("No"));
                entity.addPart("start_address",new StringBody(str_start_address));
                entity.addPart("end_address",new StringBody(str_end_address));
                entity.addPart("cross_cities",new StringBody(str_cross_cities));
                entity.addPart("time_desc",new StringBody(str_morning_time));

                entity.addPart("bus_photo", new FileBody(imageFile));


                //    totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if(prgDialog!=null){
                prgDialog.cancel();
            }
            //   Toast.makeText(Timeline_NewPost_Activity.this, responseString, Toast.LENGTH_LONG).show();
            System.out.println("=============result=============="+result);
            super.onPostExecute(result);
            JSONObject jsonObj = null;  String res =null;

            if(result.equals("success")){
                Intent in=new Intent(LoginActivity3_Activity.this,MapsActivity.class);
                startActivity(in);
            }




        }

    }

}
