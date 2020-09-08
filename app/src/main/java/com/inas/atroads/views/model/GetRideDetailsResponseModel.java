
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetRideDetailsResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("output")
    private List<Output> mOutput;
    @SerializedName("status")
    private int mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Output> getOutput() {
        return mOutput;
    }

    public void setOutput(List<Output> output) {
        mOutput = output;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public class Output {

        @SerializedName("ride_id")
        private int mRideId;
        @SerializedName("ride_time_stamp")
        private String mRideTimeStamp;
        @SerializedName("share_with")
        private Long mShareWith;
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

        public int getRideId() {
            return mRideId;
        }

        public void setRideId(int rideId) {
            mRideId = rideId;
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

    }


}
