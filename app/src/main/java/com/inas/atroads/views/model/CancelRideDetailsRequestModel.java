
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CancelRideDetailsRequestModel {

    @SerializedName("ride_id")
    private int mRideId;
    @SerializedName("user_id")
    private int mUserId;

    public int getRideId() {
        return mRideId;
    }

    public void setRideId(int rideId) {
        mRideId = rideId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
