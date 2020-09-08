
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RidesHistoryResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private Long mStatus;

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

    public class Result {

        @SerializedName("auto_number")
        private String mAutoNumber;
        @SerializedName("fullname")
        private String mFullname;
        @SerializedName("payable_amount")
        private Double mPayableAmount;
        @SerializedName("ride_time_stamp")
        private String mRideTimeStamp;
        @SerializedName("share_with")
        private Long mShareWith;
        @SerializedName("user_dest_address")
        private String mUserDestAddress;
        @SerializedName("user_dest_lat_long")
        private List<Double> mUserDestLatLong;
        @SerializedName("user_id")
        private Long mUserId;
        @SerializedName("user_source_address")
        private String mUserSourceAddress;
        @SerializedName("user_source_lat_long")
        private List<Double> mUserSourceLatLong;
        @SerializedName("vehicle_type")
        private String mVehicleType;

        public String getAutoNumber() {
            return mAutoNumber;
        }

        public void setAutoNumber(String autoNumber) {
            mAutoNumber = autoNumber;
        }

        public String getFullname() {
            return mFullname;
        }

        public void setFullname(String fullname) {
            mFullname = fullname;
        }

        public Double getPayableAmount() {
            return mPayableAmount;
        }

        public void setPayableAmount(Double payableAmount) {
            mPayableAmount = payableAmount;
        }

        public String getRideTimeStamp() {
            return mRideTimeStamp;
        }

        public void setRideTimeStamp(String rideTimeStamp) {
            mRideTimeStamp = rideTimeStamp;
        }

        public Long getShareWith() {
            return mShareWith;
        }

        public void setShareWith(Long shareWith) {
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

        public Long getUserId() {
            return mUserId;
        }

        public void setUserId(Long userId) {
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

    }


}
