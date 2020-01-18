package com.fitscorp.apps.indika.schoolbus.model;


        import java.io.Serializable;
        import java.util.List;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;


public class ParentsDataModel implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<ParentObj> data = null;
    private final static long serialVersionUID = 8859009251529085572L;

    public List<ParentObj> getData() {
        return data;
    }

    public void setData(List<ParentObj> data) {
        this.data = data;
    }

}