package com.ta.slk.sistemlayanankegiatan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activities{

    @SerializedName("id_activity")
    @Expose
    private String idActivity;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("name_activities")
    @Expose
    private String nameActivities;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("picture")
    @Expose
    private String picture;

    public String getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(String idActivity) {
        this.idActivity = idActivity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getNameActivities() {
        return nameActivities;
    }

    public void setNameActivities(String nameActivities) {
        this.nameActivities = nameActivities;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}