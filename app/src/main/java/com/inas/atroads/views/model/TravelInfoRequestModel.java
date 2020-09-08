package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelInfoRequestModel {


    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("pickup")
    @Expose
    private String pickup;
    @SerializedName("droping")
    @Expose
    private String droping;
    @SerializedName("sharing")
    @Expose
    private Integer sharing;
    @SerializedName("vehical")
    @Expose
    private String vehical;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDroping() {
        return droping;
    }

    public void setDroping(String droping) {
        this.droping = droping;
    }

    public Integer getSharing() {
        return sharing;
    }

    public void setSharing(Integer sharing) {
        this.sharing = sharing;
    }

    public String getVehical() {
        return vehical;
    }

    public void setVehical(String vehical) {
        this.vehical = vehical;
    }

}
