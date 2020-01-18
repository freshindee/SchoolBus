package com.fitscorp.apps.indika.schoolbus.login;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fitscorp.apps.indika.schoolbus.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText txt_oner_name,txt_oner_phone,txt_oner_email,txt_oner_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      //  android:id="@+id/"

        txt_oner_name=findViewById(R.id.txt_oner_name);
        txt_oner_phone=findViewById(R.id.txt_oner_phone);
        txt_oner_email=findViewById(R.id.txt_oner_email);
        txt_oner_address=findViewById(R.id.txt_oner_address);


    }

    public void nextToRouteDetaila(View view) {
        String str_oner_name,str_oner_phone,str_oner_email,str_oner_address;
        str_oner_name= txt_oner_name.getText().toString();
        str_oner_phone= txt_oner_phone.getText().toString();
        str_oner_email= txt_oner_email.getText().toString();
        str_oner_address= txt_oner_address.getText().toString();

        Intent in=new Intent(LoginActivity.this,LoginActivity2_Activity.class);
        in.putExtra("str_oner_name", str_oner_name);
        in.putExtra("str_oner_phone", str_oner_phone);
        in.putExtra("str_oner_email", str_oner_email);
        in.putExtra("str_oner_address", str_oner_address);
        startActivity(in);
        //schoolID = getIntent().getStringExtra("schoolID");
    }


}
