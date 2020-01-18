package com.fitscorp.apps.indika.schoolbus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChildMainData implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent_id")
    @Expose
    private Object parentId;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("is_coming_today")
    @Expose
    private Object isComingToday;
    @SerializedName("notification")
    @Expose
    private Object notification;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("grade")
    @Expose
    private String grade;
    private final static long serialVersionUID = 3334041227661781099L;

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

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Object getIsComingToday() {
        return isComingToday;
    }

    public void setIsComingToday(Object isComingToday) {
        this.isComingToday = isComingToday;
    }

    public Object getNotification() {
        return notification;
    }

    public void setNotification(Object notification) {
        this.notification = notification;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
