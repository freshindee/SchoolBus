package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class SchoolListMainResponse implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<SchoolListData> data = null;
    private final static long serialVersionUID = 8859009251529085572L;

    public List<SchoolListData> getData() {
        return data;
    }

    public void setData(List<SchoolListData> data) {
        this.data = data;
    }

}