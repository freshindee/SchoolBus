package com.fitscorp.apps.indika.schoolbus.login;

        import android.Manifest;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;

        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.*;

        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import com.fitscorp.apps.indika.schoolbus.*;
        import com.fitscorp.apps.indika.schoolbus.model.ChildMainData;
        import com.fitscorp.apps.indika.schoolbus.model.ChildMainResponse;
        import com.fitscorp.apps.indika.schoolbus.model.ParentObj;
        import com.fitscorp.apps.indika.schoolbus.model.ParentsDataModel;
        import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;
        import com.fitscorp.apps.indika.schoolbus.model.SchoolListMainResponse;
        import com.fitscorp.apps.indika.schoolbus.rest.AndroidMultiPartEntity;
        import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface;
        import com.fitscorp.apps.indika.schoolbus.rest.PrefManager;
        import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.gson.Gson;
        import com.mikhaellopez.lazydatepicker.LazyDatePicker;

        import java.io.*;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

        import androidx.appcompat.app.AppCompatActivity;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.entity.mime.content.FileBody;
        import org.apache.http.entity.mime.content.StringBody;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.http.Field;

public class AddNewChild extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ApiInterface apiService;

    EditText txt_fname,txt_phone,txt_email,txt_city;
    ImageButton btnDate;
    TextView btn_FeMale,btn_Male,gender,lbl_date;
    String strGender;
    boolean isDialogPopup=false;
    String snewGender,birthday_db;
    String sDate,sMonth,sYear;
    String name,s_phone,parent_id,grade,school;
    android.app.AlertDialog dialogView;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    int SELECT_FILE = 1;
    String image_absolute_path, userid_ExistingUser;
    Uri selectedImageUri;
    File imageFile;

    int selectYear, year, month, day;
    List<String> categories;

    Calendar calendar;
    ImageView img_bus_image;
    List<SchoolListData> dataList;
    ProgressDialog prgDialog;
    TextView txt_upload_image;
    boolean firsttime=false;

    EditText txt_grade;
    boolean male=false;
    boolean female=false;
    Spinner spinner,spinner_grade;
    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_child);


        pref=new PrefManager(AddNewChild.this);
        parent_id=  pref.getParent().get("parent_id");

        prgDialog=new ProgressDialog(AddNewChild.this);

        getSchoolListByDistrict();

        categories = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.spinner);



        txt_upload_image= findViewById(R.id.txt_upload_image);
        txt_fname = findViewById(R.id.txt_fname);
        lbl_date = findViewById(R.id.lbl_date);
        txt_grade = findViewById(R.id.txt_grade);
        btn_FeMale =(TextView) findViewById(R.id.btn_FeMale);
        btn_Male =(TextView) findViewById(R.id.btn_Male);
        gender =(TextView) findViewById(R.id.lbl_gender);

        img_bus_image=findViewById(R.id.img_bus_image);
        img_bus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard();
            }
        });

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




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        school= dataList.get(position).getId();
       // school=  categories.get(position);
        //  school

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getSchoolListByDistrict() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String district_id="Colombo";

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<SchoolListMainResponse> call = apiService.getSchoolList(district_id);
        call.enqueue(new Callback<SchoolListMainResponse>() {
            @Override
            public void onResponse(Call<SchoolListMainResponse> call, Response<SchoolListMainResponse> response) {
                progressDialog.cancel();

                if(response.isSuccessful()) {

                    dataList= response.body().getData();

                    if((dataList!=null) && (dataList.size()>0)){

                        for(int i=0;i<dataList.size();i++){
                            categories.add(dataList.get(i).getName());                   }

                        spinner.setOnItemSelectedListener(AddNewChild.this);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddNewChild.this, R.layout.spinner_item_schools,categories);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);

                    }

                    ///  displayView(dataList);
                }
            }
            @Override
            public void onFailure(Call<SchoolListMainResponse> call, Throwable t) {
                progressDialog.cancel();

                System.out.println("========t======"+t);
            }
        });
    }


    public void selectDate(View view) {
        showAlert_Add(AddNewChild.this);
    }
    public void selectDate() {
        showAlert_Add(AddNewChild.this);
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
                String birthYear="";
//                Date date1 = null;
//                try {
//                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(birthYear);
//                    birthday_db
//                }catch(Exception e){
//                    e.printStackTrace();
//                }

            }
        });
        dialogView.show();
    }


    public void AddChild(View view) {

        name=txt_fname.getText().toString();
        grade=txt_grade.getText().toString();
        if(imageFile==null){
            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_LONG).show();
            return;
        }

        if(name.equals("")){
            Toast.makeText(getApplicationContext(), R.string.toast_firstname_empty, Toast.LENGTH_LONG).show();
            return;
        }

        if(grade.equals("")){
            Toast.makeText(getApplicationContext(), "Grade can not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if(birthday_db==null){
            Toast.makeText(getApplicationContext(), R.string.toast_birthday_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if (snewGender==null){
            Toast.makeText(getApplicationContext(), R.string.toast_gender_empty, Toast.LENGTH_LONG).show();
            return;
        }
        else{
            snewGender=snewGender.trim();
            //=============SERVICE CALL=================================
         //   registerUser(name,parent_id,grade,school,birthday_db,snewGender);
            new UploadImage().execute();
            //=============SERVICE CALL=================================
        }
    }

    private class UploadImage extends AsyncTask<Void, Integer, String> {

        String responseString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
            prgDialog.setMessage("Uploading data. Please wait...");
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String URL="http://srilankatraveldeals.com/kuruvi/api/public/";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL+"registerchild");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //   publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("name",new StringBody(name));
                entity.addPart("parent_id",new StringBody(parent_id));
                entity.addPart("grade",new StringBody(grade));
                entity.addPart("school",new StringBody(school));
                entity.addPart("birthday",new StringBody(birthday_db));
                entity.addPart("gender",new StringBody(snewGender));
                entity.addPart("child_photo", new FileBody(imageFile));

                httppost.setEntity(entity);
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

            if(result!=null) {

                Intent in=new Intent(AddNewChild.this,MyChildrenList_Activity.class);
                startActivity(in);
                finish();
            }

        }

    }


    public void registerUser(String name,String parent_id,String grade, String school,String birthday_db,String gender) {

        final ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.show();
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);

        String parentid=pref.getParent().get("parent_id");

     //   name,parent_id,grade,school,birthday_db,snewGender

        Call<ChildMainResponse> call = apiService.registerChild(name,parentid,grade,school,birthday_db,gender);
        call.enqueue(new Callback<ChildMainResponse>() {
            @Override
            public void onResponse(Call<ChildMainResponse> call, Response<ChildMainResponse> response) {
                progress.cancel();
                if(response.isSuccessful()) {

                    Intent in=new Intent(AddNewChild.this,MyChildrenList_Activity.class);
                    startActivity(in);
                    finish();

                }
            }
            @Override
            public void onFailure(Call<ChildMainResponse> call, Throwable t) {
                progress.cancel();

                System.out.println("========t======"+t);
            }
        });
    }

    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(AddNewChild.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (AddNewChild.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

//                Snackbar.make(LoginActivity3_Activity.this.findViewById(android.R.id.content),
//                        "This app needs storage permission.",
//                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (Build.VERSION.SDK_INT >= 23)
//                                    requestPermissions(
//                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                            PERMISSIONS_MULTIPLE_REQUEST);
//                            }
//                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            selectImagePopup();


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectImagePopup();

                } else {
                    // write your logic code if permission already granted
                    //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void selectImagePopup() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            data.getData();
            selectedImageUri = data.getData();
            Cursor cursor;
            String[] projection = {MediaStore.MediaColumns.DATA};
            String selectedImagePath = null;

            cursor = AddNewChild.this.getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
                image_absolute_path = selectedImagePath;

                imageFile = new File(image_absolute_path);
            }

            imageFile = resizeAndCompressImageBeforeSend(AddNewChild.this, image_absolute_path, "postImage.jpeg");

            txt_upload_image.setText("");
            img_bus_image.setImageBitmap(BitmapFactory.decodeFile(image_absolute_path));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 200 * 200; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);

        Bitmap bmpPicNew = null;

        int compressQuality = 5; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {

            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpPic.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sp = context.getCacheDir() + "/" + fileName;
        //return the path of resized and compressed file
        return destination;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

}
