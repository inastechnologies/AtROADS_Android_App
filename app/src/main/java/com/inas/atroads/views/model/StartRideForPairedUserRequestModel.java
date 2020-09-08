
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class StartRideForPairedUserRequestModel {

    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("user_ride_id")
    private int mUserRideId;
//    @SerializedName("p_start_lat_long")
//    private String p_start_lat_long;

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

//    public String getP_start_lat_long() {
//        return p_start_lat_long;
//    }
//
//    public void setP_start_lat_long(String p_start_lat_long) {
//        this.p_start_lat_long = p_start_lat_long;
//    }
}
