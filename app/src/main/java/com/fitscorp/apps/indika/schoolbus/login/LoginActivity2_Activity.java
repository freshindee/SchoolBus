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
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.Ram;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity2_Activity extends AppCompatActivity {
    String str_oner_name,str_oner_phone,str_oner_email,str_oner_address,str_bus_number,str_driver_name,str_driver_phone,time_desc,cross_cities,end_address,start_address;
    String URL = null;
    EditText txt_bus_number,txt_driver_name,txt_driver_phone;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    int SELECT_FILE = 1;
    String image_absolute_path, userid_ExistingUser;
    Uri selectedImageUri;
    File imageFile;
    ImageView img_bus_image;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity2_);

        str_oner_name=null;
        str_oner_phone=null;
        str_oner_email=null;
        str_oner_address=null;
        str_bus_number=null;
        str_driver_name=null;
        str_driver_phone=null;
        time_desc=null;
        cross_cities=null;
        end_address=null;
        start_address=null;

        str_oner_name = getIntent().getStringExtra("str_oner_name");
        str_oner_phone = getIntent().getStringExtra("str_oner_phone");
        str_oner_email = getIntent().getStringExtra("str_oner_email");
        str_oner_address = getIntent().getStringExtra("str_oner_address");

        txt_bus_number=findViewById(R.id.txt_bus_number);
        txt_driver_name=findViewById(R.id.txt_driver_name);
        txt_driver_phone=findViewById(R.id.txt_driver_phone);

        img_bus_image=findViewById(R.id.img_bus_image);
        img_bus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard();
            }
        });


        prgDialog=new ProgressDialog(LoginActivity2_Activity.this);

    }



    public void nextToDriverDetails(View view) {

            str_bus_number= txt_bus_number.getText().toString();
            str_driver_name= txt_driver_name.getText().toString();
            str_driver_phone= txt_driver_phone.getText().toString();

            Ram.setBusImage(imageFile);

            Intent in=new Intent(LoginActivity2_Activity.this,LoginActivity3_Activity.class);
            in.putExtra("str_oner_name", str_oner_name);
            in.putExtra("str_oner_phone", str_oner_phone);
            in.putExtra("str_oner_email", str_oner_email);
            in.putExtra("str_oner_address", str_oner_address);
            in.putExtra("str_bus_number", str_bus_number);
            in.putExtra("str_driver_name", str_driver_name);
            in.putExtra("str_driver_phone", str_driver_phone);
            in.putExtra("str_driver_phone", str_driver_phone);


            startActivity(in);


        }



    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(LoginActivity2_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (LoginActivity2_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


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

            cursor = LoginActivity2_Activity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
                image_absolute_path = selectedImagePath;

                imageFile = new File(image_absolute_path);
            }

            imageFile = resizeAndCompressImageBeforeSend(LoginActivity2_Activity.this, image_absolute_path, "postImage.jpeg");

            //   showAlert_PostImage(LoginActivity3_Activity.this, "Post image?");
           // uploadFile(selectedImageUri);

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
