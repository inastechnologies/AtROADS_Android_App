
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetRideDetailsUpdateRequestModel {

//    @SerializedName("ride_date_time")
//    private String mRideDateTime;
    @SerializedName("share_with")
    private int mShareWith;
    @SerializedName("user_dest_address")
    private String mUserDestAddress;
    @SerializedName("user_dest_lat_long")
    private String mUserDestLatLong;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("user_source_address")
    private String mUserSourceAddress;
    @SerializedName("user_source_lat_long")
    private String mUserSourceLatLong;
    @SerializedName("vehicle_type")
    private String mVehicleType;

//    public String getRideDateTime() {
//        return mRideDateTime;
//    }
//
//    public void setRideDateTime(String rideDateTime) {
//        mRideDateTime = rideDateTime;
//    }

    public int getShareWith() {
        return mShareWith;
    }

    public void setShareWith(int shareWith) {
        mShareWith = shareWith;
    }

    public String getUserDestAddress() {
        return mUserDestAddress;
    }

    public void setUserDestAddress(String userDestAddress) {
        mUserDestAddress = userDestAddress;
    }

    public String getUserDestLatLong() {
        return mUserDestLatLong;
    }

    public void setUserDestLatLong(String userDestLatLong) {
        mUserDestLatLong = userDestLatLong;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserSourceAddress() {
        return mUserSourceAddress;
    }

    public void setUserSourceAddress(String userSourceAddress) {
        mUserSourceAddress = userSourceAddress;
    }

    public String getUserSourceLatLong() {
        return mUserSourceLatLong;
    }

    public void setUserSourceLatLong(String userSourceLatLong) {
        mUserSourceLatLong = userSourceLatLong;
    }

    public String getVehicleType() {
        return mVehicleType;
    }

    public void setVehicleType(String vehicleType) {
        mVehicleType = vehicleType;
    }

}
