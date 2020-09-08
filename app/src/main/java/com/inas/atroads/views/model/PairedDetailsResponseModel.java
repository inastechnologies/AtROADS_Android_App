
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PairedDetailsResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private int mStatus;

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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    @SuppressWarnings("unused")
    public class Result {

        @SerializedName("attention")
        private String mAttention;
        @SerializedName("pstatus")
        private int mPstatus;
        @SerializedName("user_ride_id")
        private int user_ride_id;
        @SerializedName("share_with")
        private int mShareWith;
        @SerializedName("user_dest_address")
        private String mUserDestAddress;
        @SerializedName("user_dest_lat_long")
        private List<Double> mUserDestLatLong;
        @SerializedName("user_id")
        private int mUserId;
        @SerializedName("user_source_address")
        private String mUserSourceAddress;
        @SerializedName("user_source_lat_long")
        private List<Double> mUserSourceLatLong;
        @SerializedName("vehicle_type")
        private String mVehicleType;

        public String getAttention() {
            return mAttention;
        }

        public void setAttention(String attention) {
            mAttention = attention;
        }

        public int getShareWith() {
            return mShareWith;
        }

        public void setShareWith(int shareWith) {
            mShareWith = shareWith;
        }

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

        public String getVehicleType() {
            return mVehicleType;
        }

        public void setVehicleType(String vehicleType) {
            mVehicleType = vehicleType;
        }

        public int getmPstatus() {
            return mPstatus;
        }

        public void setmPstatus(int mPstatus) {
            this.mPstatus = mPstatus;
        }

        public int getUser_ride_id() {
            return user_ride_id;
        }

        public void setUser_ride_id(int user_ride_id) {
            this.user_ride_id = user_ride_id;
        }
    }

}
