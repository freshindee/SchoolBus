package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GPSMainData implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longi")
    @Expose
    private String longi;
    @SerializedName("bus_id")
    @Expose
    private String busId;
    private final static long serialVersionUID = -4638379891540623340L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

}