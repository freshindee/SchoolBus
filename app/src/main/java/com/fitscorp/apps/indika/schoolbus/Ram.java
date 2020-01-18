package com.fitscorp.apps.indika.schoolbus;

import com.fitscorp.apps.indika.schoolbus.model.SchoolBusData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;

import java.io.File;
import java.util.List;

public class Ram {


   public  static SchoolBusData busObject;
    public static String districtName;
    public static String cityName;
    public static List<SchoolListData> schoolDataList;
    public static File busImage;


    public static String schoolLat;
    public static String schoolLongi;

    public static String getSchoolLat() {
        return schoolLat;
    }

    public static void setSchoolLat(String schoolLat) {
        Ram.schoolLat = schoolLat;
    }

    public static String getSchoolLongi() {
        return schoolLongi;
    }

    public static void setSchoolLongi(String schoolLongi) {
        Ram.schoolLongi = schoolLongi;
    }

    public static SchoolBusData getBusObject() {
        return busObject;
    }

    public static void setBusObject(SchoolBusData busObject) {
        Ram.busObject = busObject;
    }

    public static List<SchoolListData> getSchoolDataList() {
        return schoolDataList;
    }

    public static void setSchoolDataList(List<SchoolListData> schoolDataList) {
        Ram.schoolDataList = schoolDataList;
    }

    public static File getBusImage() {
        return busImage;
    }

    public static void setBusImage(File busImage) {
        Ram.busImage = busImage;
    }

    public static String getDistrictName() {
        return districtName;
    }

    public static void setDistrictName(String districtName) {
        Ram.districtName = districtName;
    }

    public static String getCityName() {
        return cityName;
    }

    public static void setCityName(String cityName) {
        Ram.cityName = cityName;
    }
}
