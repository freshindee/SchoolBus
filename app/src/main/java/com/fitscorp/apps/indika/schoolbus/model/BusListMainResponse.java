package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class BusListMainResponse implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<SchoolBusData> data = null;
    private final static long serialVersionUID = 8859009251529085572L;

    public List<SchoolBusData> getData() {
        return data;
    }

    public void setData(List<SchoolBusData> data) {
        this.data = data;
    }

}