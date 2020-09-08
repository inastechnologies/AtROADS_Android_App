
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RouteSourceDestResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("user_ride_id")
    private Long mUserRideId;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Result> getResult() {
        return mResult;
    }

    public void setResult(List<Result> result) {
        mResult = result;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public Long getUserRideId() {
        return mUserRideId;
    }

    public void setUserRideId(Long userRideId) {
        mUserRideId = userRideId;
    }

    @SuppressWarnings("unused")
    public class Result {

        @SerializedName("user_dest_address")
        private String mUserDestAddress;
        @SerializedName("user_dest_lat_long")
        private List<Double> mUserDestLatLong;
        @SerializedName("user_id")
        private int mUserId;
        @SerializedName("profile_pic")
        private String profile_pic;
        @SerializedName("user_source_address")
        private String mUserSourceAddress;
        @SerializedName("user_source_lat_long")
        private List<Double> mUserSourceLatLong;

        public String getUserDestAddress() {
            return mUserDestAddress;
        }

        public void setUserDestAddress(String userDestAddress) {
            mUserDestAddress = userDestAddress;
        }

        public List<Double> getUserDestLatLong() {
            return mUserDestLatLong;
        }

        public void setUserDestLatLong(List<Double> userDestLatLong) {
            mUserDestLatLong = userDestLatLong;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getUserSourceAddress() {
            return mUserSourceAddress;
        }

        public void setUserSourceAddress(String userSourceAddress) {
            mUserSourceAddress = userSourceAddress;
        }

        public List<Double> getUserSourceLatLong() {
            return mUserSourceLatLong;
        }

        public void setUserSourceLatLong(List<Double> userSourceLatLong) {
            mUserSourceLatLong = userSourceLatLong;
        }

    }

}
