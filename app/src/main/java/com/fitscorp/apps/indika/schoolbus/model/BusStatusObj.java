package com.fitscorp.apps.indika.schoolbus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusStatusObj implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bus_id")
    @Expose
    private String busId;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("school_id")
    @Expose
    private String schoolId;
    @SerializedName("child_id")
    @Expose
    private String childId;
    private final static long serialVersionUID = 7817773953332027315L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

}