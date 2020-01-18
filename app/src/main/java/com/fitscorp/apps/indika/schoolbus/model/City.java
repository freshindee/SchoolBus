package com.fitscorp.apps.indika.schoolbus.model;


        import java.io.Serializable;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class City implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("district")
    @Expose
    private String district;
    private final static long serialVersionUID = 4453517888609303870L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

}