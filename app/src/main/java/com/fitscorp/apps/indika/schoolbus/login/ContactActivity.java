package com.fitscorp.apps.indika.schoolbus.login;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    EditText txt_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        txt_message=findViewById(R.id.txt_message);
    }

    public void sendMessage(View view) {

        String address=txt_message.getText().toString();
        if(address.length()<3){
            Toast.makeText(this,"Please enter message",Toast.LENGTH_LONG).show();
        }else{
            sendmessage(address);
        }
    }


    public void sendmessage(String msg) {

        PrefManager pref=new PrefManager(ContactActivity.this);
       String user_id= pref.getParent().get("parent_id");
        final ProgressDialog progress=new ProgressDialog(ContactActivity.this);
        progress.setMessage("Please wait...");
        progress.show();
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.sendmessage(msg,user_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.cancel();
                if(response.isSuccessful()) {

                    showAlert_Add(ContactActivity.this, "Thank You . We will contact you soon");

                    System.out.println("==============");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.cancel();
                Toast.makeText(getApplicationContext(), "Request Fail. Try again later", Toast.LENGTH_LONG).show();

                System.out.println("========t======"+t);
            }
        });
    }



    public void showAlert_Add(Context c, String msg){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                finish();
            }
        });
        dialog.show();
    }


}
