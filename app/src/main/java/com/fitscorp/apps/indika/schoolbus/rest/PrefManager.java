package com.fitscorp.apps.indika.schoolbus.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by Ravi on 08/07/15.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "indika.school_bus";

    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_APP_VERSION = "app_version";
    private static final String KEY_QR_LINK = "qr_code";
    private static final String KEY_UID = "uid";

    private static final String KEY_BUS_LAT = "lat";
    private static final String KEY_BUS_LONGI = "longi";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_HASH_KEY = "hashkey";
    private static final String KEY_IMAGE_PATH = "image_path";

    private static final String KEY_FILM_HALL_NAME = "filmhall_name";
    private static final String KEY_FILM_NAME = "film_name";
    private static final String KEY_FILM_DATE = "film_date";
    private static final String KEY_FILM_TIME = "film_time";
    private static final String KEY_FILM_TICKET_ADULT = "film_ticket_big";
    private static final String KEY_FILM_TICKET_SMALL = "film_ticket_small";

    private static final String KEY_HAS_A_BUS = "is_bus_registered";

    private static final String KEY_STUDENT_ID = "student_id";

    private static final String KEY_HAVE_STUDENT = "has_student";
    private static final String KEY_HAVE_PARENT ="has_parent";
    private static final String KEY_HAVE_BUS = "has_bus";
    private static final String KEY_CITY_NAME = "city_name";


    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_PARENT_NAME = "parent_name";
    private static final String KEY_PARENT_PHONE = "parent_phone";
    private static final String KEY_PARENT_EMAIL = "parent_email";
    private static final String KEY_PARENT_CITY = "parent_city";

    private static final String KEY_PARENT_DISTRICT = "parent_district";
    private static final String KEY_PARENT_AGE = "parent_age";
    private static final String KEY_PARENT_GENDER = "parent_gender";

    private static final String KEY_BUS_ID = "bus_id";

   // private static final String KEY_HAS_A_BUS = "is_bus_registered";

   // private static final String KEY_USER_REGISTER_STATUS = "userRegisterStatus";

    private static final String KEY_BUS_OWNER_NAME = "str_oner_name";
    private static final String KEY_BUS_OWNER_ADDRESS = "str_oner_address";
    private static final String KEY_BUS_OWNER_PHONE = "str_oner_phone";
    private static final String KEY_BUS_OWNER_EMAIL = "str_oner_email";

    private static final String KEY_BUS_DRIVER_NAME = "str_driver_name";
    private static final String KEY_BUS_DRIVER_PHONE = "str_driver_phone";

    private static final String KEY_BUS_NUMBER = "str_bus_number";
    private static final String KEY_BUS_PHOTO = "str_bus_number_photo";

    private static final String KEY_BUS_START_ADDRESS = "str_start_address";
    private static final String KEY_BUS_END_ADDRESS = "str_end_address";
    private static final String KEY_BUS_CROSS_CITIES = "str_cross_cities";
    private static final String KEY_BUS_MORNING_TIME = "str_morning_time";
    private static final String KEY_BUS_EVENING_TIME = "str_evening_time";



    public void setParent(String parent_id,String parent_name,String parent_phone, String parent_email,String parent_city, String parent_district,
                               String parent_age,String parent_gender) {

        editor.putString(KEY_PARENT_ID, parent_id);
        editor.putString(KEY_PARENT_NAME, parent_name);
        editor.putString(KEY_PARENT_PHONE, parent_phone);
        editor.putString(KEY_PARENT_EMAIL, parent_email);
        editor.putString(KEY_PARENT_CITY, parent_city);
        editor.putString(KEY_PARENT_DISTRICT, parent_district);
        editor.putString(KEY_PARENT_AGE, parent_age);
        editor.putString(KEY_PARENT_GENDER, parent_gender);

        editor.commit();
    }
    public HashMap<String, String> getParent() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("parent_id", pref.getString(KEY_PARENT_ID, ""));
        profile.put("parent_name", pref.getString(KEY_PARENT_NAME, ""));
        profile.put("parent_phone", pref.getString(KEY_PARENT_PHONE, ""));
        profile.put("parent_email", pref.getString(KEY_PARENT_EMAIL, ""));
        profile.put("parent_city", pref.getString(KEY_PARENT_CITY, ""));
        profile.put("parent_district", pref.getString(KEY_PARENT_DISTRICT, ""));
        profile.put("parent_age", pref.getString(KEY_PARENT_AGE, ""));
        profile.put("parent_gender", pref.getString(KEY_PARENT_GENDER, ""));
        return profile;
    }
    public void setBusRegistered(String fd) {
        editor.putString(KEY_HAS_A_BUS, fd);
        editor.commit();
    }
    public String getBusRegistered() {
        return pref.getString(KEY_HAS_A_BUS, "");
    }
    public void createBusOwner(String busID,String str_oner_name,String str_oner_address, String str_oner_phone, String str_oner_email,
                               String str_driver_name,String str_driver_phone,String str_bus_number,String str_bus_number_photo,
                               String str_start_address,String str_end_address,String str_cross_cities,String str_morning_time,
                               String str_evening_time) {


        editor.putString(KEY_BUS_ID, busID);
        editor.putString(KEY_BUS_OWNER_NAME, str_oner_name);
        editor.putString(KEY_BUS_OWNER_ADDRESS, str_oner_address);
        editor.putString(KEY_BUS_OWNER_PHONE, str_oner_phone);
        editor.putString(KEY_BUS_OWNER_EMAIL, str_oner_email);
        editor.putString(KEY_BUS_DRIVER_NAME, str_driver_name);

        editor.putString(KEY_BUS_DRIVER_PHONE, str_driver_phone);
        editor.putString(KEY_BUS_NUMBER, str_bus_number);
        editor.putString(KEY_BUS_PHOTO, str_bus_number_photo);
        editor.putString(KEY_BUS_START_ADDRESS, str_start_address);
        editor.putString(KEY_BUS_END_ADDRESS, str_end_address);

        editor.putString(KEY_BUS_CROSS_CITIES, str_cross_cities);
        editor.putString(KEY_BUS_MORNING_TIME, str_morning_time);
        editor.putString(KEY_BUS_EVENING_TIME, str_evening_time);
        editor.commit();
    }


    public HashMap<String, String> getBusOwner() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("bus_id", pref.getString(KEY_BUS_ID, null));
        profile.put("str_oner_name", pref.getString(KEY_BUS_OWNER_NAME, null));
        profile.put("str_oner_address", pref.getString(KEY_BUS_OWNER_ADDRESS, null));
        profile.put("str_oner_phone", pref.getString(KEY_BUS_OWNER_PHONE, null));
        profile.put("str_oner_email", pref.getString(KEY_BUS_OWNER_EMAIL, null));

        profile.put("str_driver_name", pref.getString(KEY_BUS_DRIVER_NAME, ""));
        profile.put("str_driver_phone", pref.getString(KEY_BUS_DRIVER_PHONE, ""));
        profile.put("str_bus_number", pref.getString(KEY_BUS_NUMBER, null));
        profile.put("str_bus_number_photo", pref.getString(KEY_BUS_PHOTO, null));

        profile.put("str_start_address", pref.getString(KEY_BUS_START_ADDRESS, null));
        profile.put("str_end_address", pref.getString(KEY_BUS_END_ADDRESS, null));
        profile.put("str_cross_cities", pref.getString(KEY_BUS_CROSS_CITIES, null));
        profile.put("str_morning_time", pref.getString(KEY_BUS_MORNING_TIME, null));

        return profile;
    }

    public void setLati(String fd) {
        editor.putString(KEY_BUS_LAT, fd);
        editor.commit();
    }
    public String getLat() {
        return pref.getString(KEY_BUS_LAT, null);
    }
    public void setLongi(String fd) {
        editor.putString(KEY_BUS_LONGI, fd);
        editor.commit();
    }
    public String getLongi() {
        return pref.getString(KEY_BUS_LONGI, null);
    }

    public void setWorkerAddress(String fd) {
        editor.putString(KEY_FILM_DATE, fd);
        editor.commit();
    }
    public String getWorkerAddress() {
        return pref.getString(KEY_FILM_DATE, "Address");
    }

    public void setPrimaryDuty(String fn) {
        editor.putString(KEY_FILM_TIME, fn);
        editor.commit();
    }


    public String getPrimaryDuty() {
        return pref.getString(KEY_FILM_TIME, "Select Primary Duty");
    }

    public void setWorkerLanguage(String fn) {
        editor.putString(KEY_FILM_NAME, fn);
        editor.commit();
    }


    public String getWorkerLanguage() {
        return pref.getString(KEY_FILM_NAME, "Select Worker Language");
    }


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setStudentAvailability(Boolean yes) {
        editor.putBoolean(KEY_HAVE_STUDENT, yes);
        editor.commit();
    }
    public Boolean isStudentAvailable() {
        return pref.getBoolean(KEY_HAVE_STUDENT, false);
    }
    public void setCity(String yes) {
        editor.putString(KEY_CITY_NAME, yes);
        editor.commit();
    }
    public String getCity() {
        return pref.getString(KEY_CITY_NAME, "");
    }

    public void setBusAvailability(Boolean yes) {
        editor.putBoolean(KEY_HAVE_BUS, yes);
        editor.commit();
    }
    public Boolean isBusAvailable() {
        return pref.getBoolean(KEY_HAVE_BUS, false);
    }


    public void setParentAvailability(Boolean yes) {
        editor.putBoolean(KEY_HAVE_STUDENT, yes);
        editor.commit();
    }
    public Boolean isParentAvailable() {
        return pref.getBoolean(KEY_HAVE_STUDENT, false);
    }








    public void setStudentId(String st) {
        editor.putString(KEY_STUDENT_ID, st);
        editor.commit();
    }
    public String getStudentId() {
        return pref.getString(KEY_STUDENT_ID, "");
    }

    public void setFilmHallName(String fhn) {
        editor.putString(KEY_FILM_HALL_NAME, fhn);
        editor.commit();
    }
    public String getFilmHallName() {
        return pref.getString(KEY_FILM_HALL_NAME, "");
    }


    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }
    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, "0");
    }
    public String getQRLink() {
        return pref.getString(KEY_QR_LINK, "0");
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }
    public void setQRLink(String mobileNumber) {
        editor.putString(KEY_QR_LINK, mobileNumber);
        editor.commit();
    }
    public void setAppVersion(String mobileNumber) {
        editor.putString(KEY_APP_VERSION, mobileNumber);
        editor.commit();
    }
    public String getAppVersion() {
        return pref.getString(KEY_APP_VERSION, "0");
    }

    public void setLanguage(String mobileNumber) {
        editor.putString(KEY_LANGUAGE, mobileNumber);
        editor.commit();
    }
    public String getLanguage() {
        return pref.getString(KEY_LANGUAGE, "0");
    }
    public void setPrefName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }
    public String getPrefName() {
        return pref.getString(KEY_NAME, "");
    }
    public void createLogin(String name, String email, String mobile) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public void createLoginUser(String uid,String name, String email, String mobile,String hashkey,String image) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_HASH_KEY, hashkey);
        editor.putString(KEY_IMAGE_PATH, image);
        editor.commit();
    }
    public HashMap<String, String> getLoginUser() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        profile.put("hashkey", pref.getString(KEY_HASH_KEY, null));
        profile.put("image_path", pref.getString(KEY_IMAGE_PATH, null));

        return profile;
    }
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        return profile;
    }
}
