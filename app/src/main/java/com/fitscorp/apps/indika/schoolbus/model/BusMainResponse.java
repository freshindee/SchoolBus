package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BusMainResponse  implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<BusMainListData> data = null;
    private final static long serialVersionUID = 8859009251529085572L;

    public List<BusMainListData> getData() {
        return data;
    }

    public void setData(List<BusMainListData> data) {
        this.data = data;
    }

}