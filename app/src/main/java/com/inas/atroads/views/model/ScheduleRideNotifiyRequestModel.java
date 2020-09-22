
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ScheduleRideNotifiyRequestModel {

    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("user_ride_id")
    private int muser_ride_id;

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getuser_ride_id() {
        return muser_ride_id;
    }

    public void setuser_ride_id(int user_ride_id) {
        muser_ride_id = user_ride_id;
    }

}
