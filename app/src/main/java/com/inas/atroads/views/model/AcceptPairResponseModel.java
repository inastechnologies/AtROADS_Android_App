
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AcceptPairResponseModel {

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

        @SerializedName("paired_user")
        private List<PairedUser> mPairedUser;
        @SerializedName("ride_initiater_id")
        private RideInitiaterId mRideInitiaterId;
        @SerializedName("user_ride_id")
        private int mUserRideId;

        public List<PairedUser> getPairedUser() {
            return mPairedUser;
        }

        public void setPairedUser(List<PairedUser> pairedUser) {
            mPairedUser = pairedUser;
        }

        public RideInitiaterId getRideInitiaterId() {
            return mRideInitiaterId;
        }

        public void setRideInitiaterId(RideInitiaterId rideInitiaterId) {
            mRideInitiaterId = rideInitiaterId;
        }

        public int getUserRideId() {
            return mUserRideId;
        }

        public void setUserRideId(int userRideId) {
            mUserRideId = userRideId;
        }



        public class PairedUser {

            @SerializedName("accepting_other_gender")
            private String mAcceptingOtherGender;
            @SerializedName("attention")
            private String mAttention;
            @SerializedName("destination")
            private String mDestination;
            @SerializedName("distance")
            private Double mDistance;
            @SerializedName("gender")
            private String mGender;
            @SerializedName("name")
            private String mName;
            @SerializedName("pstatus")
            private int mPstatus;
            @SerializedName("ride_time_stamp")
            private String mRideTimeStamp;
            @SerializedName("share_with")
            private int mShareWith;
            @SerializedName("source")
            private String mSource;
            @SerializedName("user_dest_address")
            private String mUserDestAddress;
            @SerializedName("user_dest_lat_int")
            private List<Double> mUserDestLatint;
            @SerializedName("user_id")
            private int mUserId;
            @SerializedName("user_source_address")
            private String mUserSourceAddress;
            @SerializedName("user_source_lat_int")
            private List<Double> mUserSourceLatint;
            @SerializedName("vehicle_type")
            private String mVehicleType;

            public String getAcceptingOtherGender() {
                return mAcceptingOtherGender;
            }

            public void setAcceptingOtherGender(String acceptingOtherGender) {
                mAcceptingOtherGender = acceptingOtherGender;
            }

            public String getAttention() {
                return mAttention;
            }

            public void setAttention(String attention) {
                mAttention = attention;
            }

            public String getDestination() {
                return mDestination;
            }

            public void setDestination(String destination) {
                mDestination = destination;
            }

            public Double getDistance() {
                return mDistance;
            }

            public void setDistance(Double distance) {
                mDistance = distance;
            }

            public String getGender() {
                return mGender;
            }

            public void setGender(String gender) {
                mGender = gender;
            }

            public String getName() {
                return mName;
            }

            public void setName(String name) {
                mName = name;
            }

            public int getPstatus() {
                return mPstatus;
            }

            public void setPstatus(int pstatus) {
                mPstatus = pstatus;
            }

            public String getRideTimeStamp() {
                return mRideTimeStamp;
            }

            public void setRideTimeStamp(String rideTimeStamp) {
                mRideTimeStamp = rideTimeStamp;
            }

            public int getShareWith() {
                return mShareWith;
            }

            public void setShareWith(int shareWith) {
                mShareWith = shareWith;
            }

            public String getSource() {
                return mSource;
            }

            public void setSource(String source) {
                mSource = source;
            }

            public String getUserDestAddress() {
                return mUserDestAddress;
            }

            public void setUserDestAddress(String userDestAddress) {
                mUserDestAddress = userDestAddress;
            }

            public List<Double> getUserDestLatint() {
                return mUserDestLatint;
            }

            public void setUserDestLatint(List<Double> userDestLatint) {
                mUserDestLatint = userDestLatint;
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

            public List<Double> getUserSourceLatint() {
                return mUserSourceLatint;
            }

            public void setUserSourceLatint(List<Double> userSourceLatint) {
                mUserSourceLatint = userSourceLatint;
            }

            public String getVehicleType() {
                return mVehicleType;
            }

            public void setVehicleType(String vehicleType) {
                mVehicleType = vehicleType;
            }

        }


        @SuppressWarnings("unused")
        public class RideInitiaterId {

            @SerializedName("ride_initiater_id")
            private int mRideInitiaterId;
            @SerializedName("ride_paired_id")
            private int mRidePairedId;

            public int getRideInitiaterId() {
                return mRideInitiaterId;
            }

            public void setRideInitiaterId(int rideInitiaterId) {
                mRideInitiaterId = rideInitiaterId;
            }

            public int getRidePairedId() {
                return mRidePairedId;
            }

            public void setRidePairedId(int ridePairedId) {
                mRidePairedId = ridePairedId;
            }

        }

        
    }

}
