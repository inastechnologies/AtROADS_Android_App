
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EndRideRequestModel {

    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("user_ride_id")
    private int mUserRideId;
//    @SerializedName("end_lat_long")
//    private String end_lat_long;

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getUserRideId() {
        return mUserRideId;
    }

    public void setUserRideId(int userRideId) {
        mUserRideId = userRideId;
    }

//    public String getEnd_lat_long() {
//        return end_lat_long;
//    }
//
//    public void setEnd_lat_long(String end_lat_long) {
//        this.end_lat_long = end_lat_long;
//    }
}
