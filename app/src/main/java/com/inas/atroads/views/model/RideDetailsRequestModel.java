package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideDetailsRequestModel {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_source_lat_long")
    @Expose
    private String userSourceLatLong;
    @SerializedName("user_dest_lat_long")
    @Expose
    private String userDestLatLong;
    @SerializedName("user_source_address")
    @Expose
    private String userSourceAddress;
    @SerializedName("user_dest_address")
    @Expose
    private String userDestAddress;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("share_with")
    @Expose
    private Integer shareWith;
    @SerializedName("ride_date_time")
    @Expose
    private String rideDateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserSourceLatLong() {
        return userSourceLatLong;
    }

    public void setUserSourceLatLong(String userSourceLatLong) {
        this.userSourceLatLong = userSourceLatLong;
    }

    public String getUserDestLatLong() {
        return userDestLatLong;
    }

    public void setUserDestLatLong(String userDestLatLong) {
        this.userDestLatLong = userDestLatLong;
    }

    public String getUserSourceAddress() {
        return userSourceAddress;
    }

    public void setUserSourceAddress(String userSourceAddress) {
        this.userSourceAddress = userSourceAddress;
    }

    public String getUserDestAddress() {
        return userDestAddress;
    }

    public void setUserDestAddress(String userDestAddress) {
        this.userDestAddress = userDestAddress;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getShareWith() {
        return shareWith;
    }

    public void setShareWith(Integer shareWith) {
        this.shareWith = shareWith;
    }

    public String getRideDateTime() {
        return rideDateTime;
    }

    public void setRideDateTime(String rideDateTime) {
        this.rideDateTime = rideDateTime;
    }

}
