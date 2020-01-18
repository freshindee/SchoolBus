package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SchoolBusData implements Serializable

{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("owner_address")
    @Expose
    private String ownerAddress;
    @SerializedName("owner_phone")
    @Expose
    private String ownerPhone;
    @SerializedName("owner_email")
    @Expose
    private String ownerEmail;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("driver_photo")
    @Expose
    private String driverPhoto;
    @SerializedName("driver_phone")
    @Expose
    private String driverPhone;
    @SerializedName("bus_number")
    @Expose
    private String busNumber;
    @SerializedName("bus_photo")
    @Expose
    private String busPhoto;
    @SerializedName("bus_number_photo")
    @Expose
    private String busNumberPhoto;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("cross_cities")
    @Expose
    private String crossCities;
    @SerializedName("time_desc")
    @Expose
    private String timeDesc;
    private final static long serialVersionUID = 2079938224334570210L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusPhoto() {
        return busPhoto;
    }

    public void setBusPhoto(String busPhoto) {
        this.busPhoto = busPhoto;
    }

    public String getBusNumberPhoto() {
        return busNumberPhoto;
    }

    public void setBusNumberPhoto(String busNumberPhoto) {
        this.busNumberPhoto = busNumberPhoto;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getCrossCities() {
        return crossCities;
    }

    public void setCrossCities(String crossCities) {
        this.crossCities = crossCities;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

}
