
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class StartRideRequestModel {

    @SerializedName("amount")
    private Double mAmount;
    @SerializedName("auto_number")
    private String mAutoNumber;
    @SerializedName("type")
    private String mType;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("user_ride_id")
    private int mUserRideId;


    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        mAmount = amount;
    }

    public String getAutoNumber() {
        return mAutoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        mAutoNumber = autoNumber;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
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
