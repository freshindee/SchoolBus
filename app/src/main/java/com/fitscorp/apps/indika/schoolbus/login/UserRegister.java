package com.fitscorp.apps.indika.schoolbus.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;
import com.fitscorp.apps.indika.schoolbus.ShowBus_MainActivity;
import com.fitscorp.apps.indika.schoolbus.ShowSchools;
import com.fitscorp.apps.indika.schoolbus.Utility;
import com.fitscorp.apps.indika.schoolbus.model.ParentObj;
import com.fitscorp.apps.indika.schoolbus.model.ParentsDataModel;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListMainResponse;
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ApiInterface apiService;
    android.app.AlertDialog dialogView;
    EditText txt_fname,txt_phone,txt_email;
    TextView txt_city;
    ImageButton btnDate;
    TextView btn_FeMale,btn_Male,gender,lbl_date;
    String parent_city;
    String snewGender,birthday_db;
    String sDate,sMonth,sYear;
    String s_fname,s_phone,email,city,country_code,district;

    boolean isDialogPopup=false;
    String parentID;
    int selectYear, year, month, day;
    List<String> categories;
    Calendar calendar;

    LinearLayout date_picker_layout;

    ProgressDialog prgDialog;

    boolean firsttime=false;
    LinearLayout lay_next_view;
    boolean male=false;
    boolean female=false;
    String sttus,mobile,name,s_district;
    HashMap<String, String> user=null;
   // private PrefManager prefManager;
    Button btn_LetsGo;
    TextView txt_privacypolicy,txt_termscondition;
    PrefManager pref;
    LinearLayout lay_line_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#242424";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }

        setContentView(R.layout.activity_user_register);


        localConstructor();

        btn_FeMale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_Male.setBackground( getResources().getDrawable(R.drawable.transferent_boder_button));
                btn_FeMale.setBackground( getResources().getDrawable(R.drawable.selected_transferent_boder_button));
                gender.setText("Female");
                snewGender="0";
            }
        });

        btn_Male.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_Male.setBackground( getResources().getDrawable(R.drawable.selected_transferent_boder_button));
                btn_FeMale.setBackground( getResources().getDrawable(R.drawable.transferent_boder_button));
                gender.setText("Male");
                snewGender="1";


            }
        });
        btn_FeMale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_Male.setBackground( getResources().getDrawable(R.drawable.transferent_boder_button));
                btn_FeMale.setBackground( getResources().getDrawable(R.drawable.selected_transferent_boder_button));
                gender.setText("Female");
                snewGender="2";
            }
        });

    }

    public void selectDate(View view) {
        showAlert_Add(UserRegister.this);
        // datePicker1.setVisibility(View.VISIBLE);
    }
    public void showAlert_Add(Context c) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_date_picker, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        dialogView = builder.create();
        dialogView.setCancelable(false);
        final DatePicker picker=(DatePicker)layoutView.findViewById(R.id.datePicker1);


        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDialogPopup=false;
                dialogView.cancel();

                //   finish();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDialogPopup=false;
                dialogView.cancel();
                System.out.println("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
                String DateStr0= picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();
                String DateStr=picker.getYear()+ "/" +(picker.getMonth() + 1)+ "/" +picker.getDayOfMonth();
                birthday_db=DateStr;
                lbl_date.setText(DateStr);

            }
        });
        dialogView.show();
    }
    void localConstructor(){
        snewGender="";
        categories = new ArrayList<String>();
        country_code= getIntent().getStringExtra("country_code");
        categories.add("Colombo");

        //  prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        apiService=null;
        pref=new PrefManager(UserRegister.this);
        date_picker_layout=(LinearLayout) findViewById(R.id.date_picker_layout);
        btnDate =(ImageButton) findViewById(R.id.btnDate);

        lay_line_phone= findViewById(R.id.lay_line_phone);
        lay_next_view=findViewById(R.id.lay_next_view);
        txt_termscondition =(TextView) findViewById(R.id.txt_termscondition);
        txt_privacypolicy =(TextView) findViewById(R.id.txt_privacypolicy);
        btn_LetsGo =(Button) findViewById(R.id.btn_LetsGo);

        btn_LetsGo.setImeOptions(EditorInfo.IME_ACTION_DONE);

        btn_FeMale =(TextView) findViewById(R.id.btn_FeMale);
        btn_Male =(TextView) findViewById(R.id.btn_Male);
        gender =(TextView) findViewById(R.id.lbl_gender);

        lbl_date =(TextView) findViewById(R.id.lbl_date);
       // txt_slect_text =findViewById(R.id.txt_slect_text);

        txt_fname =(EditText) findViewById(R.id.txt_fname);
        txt_phone =(EditText) findViewById(R.id.txt_phone);
        txt_email =(EditText) findViewById(R.id.txt_email);
        txt_city = findViewById(R.id.txt_city);

        txt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(UserRegister.this,SelectCityActivity.class);
                startActivity(in);
            }
        });

        lay_next_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginClick();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(pref.getCity().length()>0){
            parent_city=pref.getCity();
        }
        txt_city.setText(parent_city);


        if(pref.getParent().get("parent_name").length()>0){

        String name=pref.getParent().get("parent_name");
        String parent_phone=pref.getParent().get("parent_phone");
        String parent_email=pref.getParent().get("parent_email");


        if(pref.getCity().length()>0){
                 parent_city=pref.getCity();
        }
        // parent_city=pref.getParent().get("parent_city");



        String parent_district=pref.getParent().get("parent_district");
        String parent_age=pref.getParent().get("parent_age");
        String parent_gender=pref.getParent().get("parent_gender");
        birthday_db=parent_age;

        txt_phone.setVisibility(View.GONE);
        lay_line_phone.setVisibility(View.GONE);

        txt_fname.setText(name);

        if(parent_phone.length()>0){
                txt_phone.setEnabled(false);
        }

        txt_phone.setText(parent_phone);
        txt_email.setText(parent_email);
        txt_city.setText(parent_city);

        lbl_date.setText(parent_age);
        lbl_date.setText(parent_age);

        if(parent_gender.equals("0")){
            btn_Male.setBackground( getResources().getDrawable(R.drawable.transferent_boder_button));
            btn_FeMale.setBackground( getResources().getDrawable(R.drawable.selected_transferent_boder_button));
            gender.setText("Female");
            snewGender="0";
        }else{
            btn_Male.setBackground( getResources().getDrawable(R.drawable.selected_transferent_boder_button));
            btn_FeMale.setBackground( getResources().getDrawable(R.drawable.transferent_boder_button));
            gender.setText("Male");
            snewGender="1";
        }
        }
    }

    public void Login(View v){
       LoginClick();

    }
    public void LoginClick(){
        email="";

        name=txt_fname.getText().toString();
        s_phone=txt_phone.getText().toString();
        email=txt_email.getText().toString();

        city=txt_city.getText().toString();
        email=email.trim();

        s_phone=s_phone.trim();
        s_phone = s_phone.replaceAll(" ", "");


        if(name.equals("")){
            Toast.makeText(getApplicationContext(), R.string.toast_firstname_empty, Toast.LENGTH_LONG).show();
            return;
        }
//        if(!Utility.isAlpha(name)){
//            Toast.makeText(getApplicationContext(), R.string.toast_firstname_onlycharacters, Toast.LENGTH_LONG).show();
//            return;
//        }
        if(s_phone.equals("")){
            Toast.makeText(getApplicationContext(), R.string.toast_lastname_empty, Toast.LENGTH_LONG).show();
            return;
        }

        if(city.equals("")){
            Toast.makeText(getApplicationContext(), R.string.toast_city_empty, Toast.LENGTH_LONG).show();
            return;
        }
//        if(email.equals("")){
//            Toast.makeText(getApplicationContext(), R.string.toast_email_empty, Toast.LENGTH_LONG).show();
//            return;
//        }

        if(birthday_db.equals("")){
            Toast.makeText(getApplicationContext(), R.string.toast_birthday_empty, Toast.LENGTH_LONG).show();
            return;
        }
      //  snewGender=snewGender.trim();
        if (snewGender.equals("")){
            Toast.makeText(getApplicationContext(), R.string.toast_gender_empty, Toast.LENGTH_LONG).show();
            return;
        }else{

            s_district="Colombo";

            if(Utility.isInternetAvailable(UserRegister.this)){
                //=============SERVICE CALL=================================
                registerUser(name,s_phone,email,city,birthday_db,snewGender,s_district);
                //=============SERVICE CALL=================================

            }else{
                Toast.makeText(UserRegister.this,"No internet connection",Toast.LENGTH_LONG).show();

            }

        }


    }


    public void registerUser(String name, String phone, String email, String city, String birth, String gender, final String district) {
        final ProgressDialog progress=new ProgressDialog(UserRegister.this);
        progress.setMessage("Please wait...");
        progress.show();

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<ParentsDataModel> call = apiService.registerUser(name,phone,email,city,birth,gender,district);
        call.enqueue(new Callback<ParentsDataModel>() {
            @Override
            public void onResponse(Call<ParentsDataModel> call, Response<ParentsDataModel> response) {
                progress.cancel();
                if(response.isSuccessful()) {

                    Gson gson1 = new Gson();

                    ParentsDataModel res= (ParentsDataModel) response.body();
                    List<ParentObj> dataList= res.getData();

                    if(dataList!=null){
                        ParentObj parent=dataList.get(0);
                        pref.setParentAvailability(true);
                        pref.setParent(parent.getId(),parent.getName(),parent.getPhone(),parent.getEmail(),parent.getCity(),parent.getDistrict(),parent.getBirthday(),parent.getGender());
                        parentID=parent.getId();
                    }
                    Intent in=new Intent(UserRegister.this,MyChildrenList_Activity.class);
                    in.putExtra("parentID",parentID);
                    startActivity(in);
                    finish();


//                    else if(!pref.getBusRegistered().equals("0")){
//
//                        Intent in=new Intent(UserRegister.this,MyChildrenList_Activity.class);
//                        in.putExtra("parentID",parentID);
//                        startActivity(in);
//
////                        String busid=  pref.getBusRegistered();
////                        Intent in=new Intent(UserRegister.this,ShowBus_MainActivity.class);
////                        in.putExtra("busid", busid);
////                        startActivity(in);
////                        finish();
//                    }else{
//                        Intent in=new Intent(UserRegister.this,MyChildrenList_Activity.class);
//                        in.putExtra("parentID",parentID);
//                        startActivity(in);
//
////                        Intent in=new Intent(UserRegister.this,ShowSchools.class);
////                        in.putExtra("district",district);
////                        startActivity(in);
////                        finish();
//                    }


                }
            }
            @Override
            public void onFailure(Call<ParentsDataModel> call, Throwable t) {
                progress.cancel();

                System.out.println("========t======"+t);
            }
        });
    }




    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }
    private void showDate(int year, int month, int day) {
        selectYear = year;
        birthday_db = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        sDate=Integer.toString(day);
        sMonth=Integer.toString(month);
        sYear=Integer.toString(year);

        if(firsttime) {
            lbl_date.setText(birthday_db);
        }else{
            firsttime=true;
        }
        System.out.println(",.......................date...................." + birthday_db+"  "+sDate+"    "+sMonth+"     "+sYear);

    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
        }
    };
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(UserRegister.this, myDateListener, year, month, day);
        }
        return null;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        district = parent.getItemAtPosition(position).toString();
        //txt_slect_text.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
