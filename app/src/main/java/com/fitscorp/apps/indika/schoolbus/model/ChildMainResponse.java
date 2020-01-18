package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChildMainResponse  implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<ChildMainData> data = null;
    private final static long serialVersionUID = 8859009251529085572L;

    public List<ChildMainData> getData() {
        return data;
    }

    public void setData(List<ChildMainData> data) {
        this.data = data;
    }

}