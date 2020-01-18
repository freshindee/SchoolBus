package com.fitscorp.apps.indika.schoolbus;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;


import android.util.Base64;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by appdev on 12/21/2016.
 */

public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public static boolean isContain(ArrayList<String> arr, String targetValue){
        for(int i = 0 ; i < arr.size() ; i++) {
            String currentval = arr.get(i);
            if (currentval.equals(targetValue))
                return true;
        }
        return false;
    }
    public static boolean isNeedUpdateCash(String cashType) {
        boolean isNeed=false;

        if(cashType.equals("workout")){

        }
        return isNeed;
    }
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
    public static String upperCaseWords(String docName) {
       String sentence=docName.toLowerCase();
        String words[] = sentence.replaceAll("\\s+", " ").trim().split(" ");
        String newSentence = "";
        for (String word : words) {
            for (int i = 0; i < word.length(); i++)
                newSentence = newSentence + ((i == 0) ? word.substring(i, i + 1).toUpperCase():
                        (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase() : word.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
        }

        return newSentence;
    }
    public static String appendAyuboLoginInfo(String hashkey,String urlPath) {

        String modifiedURL=null;
        if(urlPath.contains("ayubo.life")){
            boolean hasQuestionMark= urlPath.contains("?");
            boolean isHashAlreadyFound= urlPath.contains("f_login=");
            if(!isHashAlreadyFound) {
                if(hasQuestionMark){
                    modifiedURL="&f_login="+hashkey;
                    modifiedURL=urlPath+modifiedURL;
                }else{
                    modifiedURL="?f_login="+hashkey;
                    modifiedURL=urlPath+modifiedURL;
                }
            }else{
                modifiedURL=urlPath;
            }
        }else{
            modifiedURL=urlPath;
        }
        return modifiedURL;
    }


    public static ArrayList<String> getActiveArray(String data){

        ArrayList<String> journalArry = new ArrayList<String>();
        JSONArray myDataListsAll= null;
        try {
            myDataListsAll = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<myDataListsAll.length();i++) {
            String childJson=null;
            try {
                childJson = myDataListsAll.get(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            journalArry.add(childJson);
        }
        return journalArry;
    }
    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public static String getDeviceDensityName() {
        float density = Resources.getSystem().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "xhdpi";
    }
    public static int getImageSizeFor_DeviceDensitySize(int imageSize) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        float fval=(imageSize /2) * density;
        int den3 = (int) Math.round(fval);
        return den3;
    }




    public static String StreamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }

//    public static void hideSoftKeyboard(Context context) {
////        InputMethodManager inputMethodManager =
////                (InputMethodManager) context.getSystemService(
////                        context.INPUT_METHOD_SERVICE);
////        inputMethodManager.hideSoftInputFromWindow(
////                context.getCurrentFocus().getWindowToken(), 0);
////    }

//    public static void disapearKeypad(View v) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }
    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }
    public static boolean isValidNumber(String mobile) {
        String regEx = "^[0-9]$";
        return mobile.matches(regEx);
    }

    public static boolean isValidNIC(String mobile) {
        String regEx = "[0-9]{9}[x|X|v|V]$";
        return mobile.matches(regEx);
    }


    public static String getDayOfMonthSuffix(int n) {
        //  checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        String th=null;
        if (n >= 11 && n <= 13) {
            th= "th";
        }else{
        switch (n % 10) {
            case 1:  th= "st";break;
            case 2:  th= "nd";break;
            case 3:  th= "rd";break;
            default: th= "th";break;
        }}
        return th;
    }


    public static String getReadableDate(String d){
        String strDate=null;

        String[] naesList = d.split("-");
        String str_year = naesList[0];
        String str_mon = naesList[1];
        String str_dat = naesList[2];
        int da=Integer.parseInt(str_dat);


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;  String str_month=null;
        try {
            startDate = df.parse(d);
            String newDateString= new SimpleDateFormat("yyyy-MMMM-EEEE").format(startDate);
            String[] naesList2 = newDateString.split("-");
            str_month = naesList2[1];
            //  String newDateString = df.format(startDate);
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dayPrefix= getDayOfMonthSuffix(da);
        String space=" ";
        str_dat=str_dat+dayPrefix;

        strDate=str_dat+space+str_month+space+str_year;

        return strDate;
    }


    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
   // private static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)";
    private static final String EMAIL_PATTERN ="";
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }
    public static boolean isNull(String txt){
        boolean result;
        if(txt.trim().length()==0 && txt==null){
            result=true;
        }else{
            result=false;
        }
        return  result;
    }
//    public static boolean validate(String email) {
//        pattern = Pattern.compile(EMAIL_PATTERN);
//        matcher = pattern.matcher(email);
//        return matcher.matches();
//
//    }

    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return false;
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    /**
     * <p>Checks if a calendar is today.</p>
     *
     * @param calendar the calendar, not altered, not null.
     * @return true if the calendar is today.
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            throw new IllegalArgumentException("The dates must not be null");
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static int getTotalWeeks(Calendar calendar) {
        if (null == calendar) return 0;
        int maxWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return maxWeeks;

    }

    public static int getTotalWeeks(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getTotalWeeks(cal);
    }

    public static boolean isPastDay(Date date) {
        boolean ssss=false;

        date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
      //  return (date.before(calendar.getTime())) ? true : false;
        String[] someArray = new String[2];
        someArray[0] = "3";
        someArray[1] = "14";

        int dc=date.getDate();
        System.out.println("======================llllllllllllll==========="+dc);


        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2017, 2 ,dc, 3, 0, 0);


        int dateee=calendar2.DATE;

        for(String item2 : someArray) {
            System.out.println("===oooooooooooooooo==========================="+item2);

            int ter=Integer.parseInt(item2);

            if(dc==ter){
                ssss= true;
                System.out.println("===KKKKKKKKKKKKKK============================"+ter+"==============="+dateee);
            }
            else{
                ssss= false;
            }

        }

            return ssss;
        }






    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }


    public static String getMonthForInt(int num) {

        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }
    public static int getRemainingDays(String startingDate) {

        int y = Calendar.getInstance().get(Calendar.YEAR);
        int m = Calendar.getInstance().get(Calendar.MONTH)+1;
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String[] parts = startingDate.split("-");
        int fYear = Integer.parseInt(parts[0]);
        int fMonth = Integer.parseInt(parts[1]);
        int fDate = Integer.parseInt(parts[2]);

        Calendar now = Calendar.getInstance();
        Calendar fromDate = Calendar.getInstance();
        //  Calendar toDate = Calendar.getInstance();

        now.set(y, m, d);
        fromDate.set(fYear, fMonth, fDate);

        long todays= now.getTimeInMillis();
        long finDate= fromDate.getTimeInMillis();

        long diff =finDate - todays;

        int ddays = (int) (diff / (24 * 60 * 60 * 1000));

        return ddays;
    }

    public static int getDaysLeft(String finalDate) {

        int y = Calendar.getInstance().get(Calendar.YEAR);
        int m = Calendar.getInstance().get(Calendar.MONTH)+1;
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String[] parts = finalDate.split("-");
        int fYear = Integer.parseInt(parts[0]);
        int fMonth = Integer.parseInt(parts[1]);
        int fDate = Integer.parseInt(parts[2]);

        Calendar now = Calendar.getInstance();
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();

        now.set(y, m, d);
        fromDate.set(fYear, fMonth, fDate);

        long todays= now.getTimeInMillis();
        long finDate= fromDate.getTimeInMillis();

        long diff = finDate - todays;

        int ddays = (int) (diff / (24 * 60 * 60 * 1000));

        return ddays;
    }
    public static boolean isChallengeStarted(String startDate) {
        boolean status=false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startingDate = null; Date today = null;
        try {
            startingDate = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int y = Calendar.getInstance().get(Calendar.YEAR);
        int m = Calendar.getInstance().get(Calendar.MONTH)+1;
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String[] ary = startDate.split("-");
        String curDate=y+"-"+m+"-"+d;
        try {
            today = sdf.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(sdf.format(startingDate));
        System.out.println(sdf.format(today));
        if(startingDate.compareTo(today)<0){
            status=true;
            System.out.println("Date1 is after today");
        }
        else if(startingDate.compareTo(today)==0){
            status=true;
            System.out.println("Date1 is equal to today");
        }
        if(startingDate.compareTo(today)>0){
            status=false;
            System.out.println("Date1 is BEFORE today");
        }
        else{
            //  status=false;
        }

        return status;
    }

    public static boolean isValidBirthday(int pass) {
        boolean pas = false;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int y = year - pass;
        if (y <= 10) {
            pas = false;
        } else {
            pas = true;
        }

        return pas;
    }




    public static boolean isValidWeightandHeight(String pass) {
        boolean pas = false;
        if (pass.length() < 2 && pass.length() > 3) {
            pas = false;
        } else {
            pas = true;
        }
        return pas;
    }



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}
