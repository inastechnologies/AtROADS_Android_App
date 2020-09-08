
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;
@SuppressWarnings("unused")
public class MeterCalculationsRequestModel {

    @SerializedName("total_fare")
    private Double mTotalFare;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("user_ride_id")
    private int mUserRideId;

    public Double getTotalFare() {
        return mTotalFare;
    }

    public void setTotalFare(Double totalFare) {
        mTotalFare = totalFare;
    }

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

}
