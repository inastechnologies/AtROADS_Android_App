
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetDetailsOfRideRequestModel {

    @SerializedName("user_ride_id")
    private int mUserRideId;

    public int getUserRideId() {
        return mUserRideId;
    }

    public void setUserRideId(int userRideId) {
        mUserRideId = userRideId;
    }

}
