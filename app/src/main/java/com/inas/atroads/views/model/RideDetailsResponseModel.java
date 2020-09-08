package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RideDetailsResponseModel {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output = null;
    @SerializedName("result")
    @Expose
    private String result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public class Output {

        @SerializedName("ride_date_time")
        @Expose
        private String rideDateTime;
        @SerializedName("share_with")
        @Expose
        private Integer shareWith;
        @SerializedName("user_dest_address")
        @Expose
        private String userDestAddress;
        @SerializedName("user_dest_lat_long")
        @Expose
        private String userDestLatLong;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_source_address")
        @Expose
        private String userSourceAddress;
        @SerializedName("user_source_lat_long")
        @Expose
        private String userSourceLatLong;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;

        public String getRideDateTime() {
            return rideDateTime;
        }

        public void setRideDateTime(String rideDateTime) {
            this.rideDateTime = rideDateTime;
        }

        public Integer getShareWith() {
            return shareWith;
        }

        public void setShareWith(Integer shareWith) {
            this.shareWith = shareWith;
        }

        public String getUserDestAddress() {
            return userDestAddress;
        }

        public void setUserDestAddress(String userDestAddress) {
            this.userDestAddress = userDestAddress;
        }

        public String getUserDestLatLong() {
            return userDestLatLong;
        }

        public void setUserDestLatLong(String userDestLatLong) {
            this.userDestLatLong = userDestLatLong;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserSourceAddress() {
            return userSourceAddress;
        }

        public void setUserSourceAddress(String userSourceAddress) {
            this.userSourceAddress = userSourceAddress;
        }

        public String getUserSourceLatLong() {
            return userSourceLatLong;
        }

        public void setUserSourceLatLong(String userSourceLatLong) {
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
