package com.fitscorp.apps.indika.schoolbus.login;

import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.widget.Toast;
import com.fitscorp.apps.indika.schoolbus.BuildConfig;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.ShowSchools;
import com.fitscorp.apps.indika.schoolbus.Utility;
import com.fitscorp.apps.indika.schoolbus.firebase.NewMapKotlinActivity;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;

import androidx.appcompat.app.AppCompatActivity;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    PrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref=new PrefManager(this);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Utility.isInternetAvailable(SplashActivity.this)){
                    getAppVersion();
                }else{
                    Toast.makeText(SplashActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                }

            }
        }, 1000);




    }


    public void getAppVersion() {
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.getAppVersion();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    int versionCode = BuildConfig.VERSION_CODE;
                    String version= response.body();

                    if(versionCode==Integer.parseInt(version)){

                        if(pref.isBusAvailable()){
                            Intent in=new Intent(SplashActivity.this, NewMapKotlinActivity.class);
                            startActivity(in);
                            finish();
                        }
                        else if((pref.isStudentAvailable())){
                            Intent in=new Intent(SplashActivity.this, ShowSchools.class);
                            startActivity(in);
                            finish();
                        }
                        else if((pref.isParentAvailable())){
                            Intent in=new Intent(SplashActivity.this,MyChildrenList_Activity.class);
                            startActivity(in);
                            finish();
                        }else{
                            Intent in=new Intent(SplashActivity.this, ParentLoginActivity.class);
                            startActivity(in);
                            finish();
                        }
                       // pref.setStudentAvailability(true);
                    }else{
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SplashActivity.this,"Service temporarily unavailable",Toast.LENGTH_LONG).show();


            }
        });
    }

}
