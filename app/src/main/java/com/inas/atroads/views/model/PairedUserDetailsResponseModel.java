
package com.inas.atroads.views.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PairedUserDetailsResponseModel {

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

    public class Result {

        @SerializedName("attention")
        @Expose
        private String attention;
        @SerializedName("destination")
        @Expose
        private String destination;
        @SerializedName("pstatus")
        @Expose
        private Integer pstatus;
        @SerializedName("share_with")
        @Expose
        private Integer shareWith;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("user_dest_address")
        @Expose
        private String userDestAddress;
        @SerializedName("user_dest_lat_long")
        @Expose
        private List<Double> userDestLatLong = null;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_ride_id")
        @Expose
        private Integer user_ride_id;
        @SerializedName("user_source_address")
        @Expose
        private String userSourceAddress;
        @SerializedName("user_source_lat_long")
        @Expose
        private List<Double> userSourceLatLong = null;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;

        public String getAttention() {
            return attention;
        }

        public void setAttention(String attention) {
            this.attention = attention;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public Integer getPstatus() {
            return pstatus;
        }

        public void setPstatus(Integer pstatus) {
            this.pstatus = pstatus;
        }

        public Integer getShareWith() {
            return shareWith;
        }

        public void setShareWith(Integer shareWith) {
            this.shareWith = shareWith;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUserDestAddress() {
            return userDestAddress;
        }

        public void setUserDestAddress(String userDestAddress) {
            this.userDestAddress = userDestAddress;
        }

        public List<Double> getUserDestLatLong() {
            return userDestLatLong;
        }

        public void setUserDestLatLong(List<Double> userDestLatLong) {
            this.userDestLatLong = userDestLatLong;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getUser_ride_id() {
            return user_ride_id;
        }

        public void setUser_ride_id(Integer user_ride_id) {
            this.user_ride_id = user_ride_id;
        }

        public String getUserSourceAddress() {
            return userSourceAddress;
        }

        public void setUserSourceAddress(String userSourceAddress) {
            this.userSourceAddress = userSourceAddress;
        }

        public List<Double> getUserSourceLatLong() {
            return userSourceLatLong;
        }

        public void setUserSourceLatLong(List<Double> userSourceLatLong) {
            this.userSourceLatLong = userSourceLatLong;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

    }

}
