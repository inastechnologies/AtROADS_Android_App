
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class YourBillRequestModel {

    @SerializedName("id")
    private int mId;
    @SerializedName("rate_the_ride")
    private String mRateTheRide;
    @SerializedName("review")
    private String mReview;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getRateTheRide() {
        return mRateTheRide;
    }

    public void setRateTheRide(String rateTheRide) {
        mRateTheRide = rateTheRide;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }

}
