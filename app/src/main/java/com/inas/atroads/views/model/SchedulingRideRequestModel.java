
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SchedulingRideRequestModel {

    @SerializedName("from_time")
    private String mFromTime;
    @SerializedName("home")
    private String mHome;
    @SerializedName("office")
    private String mOffice;
    @SerializedName("other")
    private String mOther;
    @SerializedName("pairing")
    private int mPairing;
    @SerializedName("to_time")
    private String mToTime;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("When_you_want_to_notify")
    private int mWhenYouWantToNotify;

    public String getFromTime() {
        return mFromTime;
    }

    public void setFromTime(String fromTime) {
        mFromTime = fromTime;
    }

    public String getHome() {
        return mHome;
    }

    public void setHome(String home) {
        mHome = home;
    }

    public String getOffice() {
        return mOffice;
    }

    public void setOffice(String office) {
        mOffice = office;
    }

    public String getOther() {
        return mOther;
    }

    public void setOther(String other) {
        mOther = other;
    }

    public int getPairing() {
        return mPairing;
    }

    public void setPairing(int pairing) {
        mPairing = pairing;
    }

    public String getToTime() {
        return mToTime;
    }

    public void setToTime(String toTime) {
        mToTime = toTime;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getWhenYouWantToNotify() {
        return mWhenYouWantToNotify;
    }

    public void setWhenYouWantToNotify(int whenYouWantToNotify) {
        mWhenYouWantToNotify = whenYouWantToNotify;
    }

}
