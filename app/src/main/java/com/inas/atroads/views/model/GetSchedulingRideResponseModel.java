
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetSchedulingRideResponseModel {

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

        @SerializedName("scheduling_ride")
        private SchedulingRide mSchedulingRide;

        public SchedulingRide getSchedulingRide() {
            return mSchedulingRide;
        }

        public void setSchedulingRide(SchedulingRide schedulingRide) {
            mSchedulingRide = schedulingRide;
        }

    }

    public class SchedulingRide {

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

        public int getWhenYouWantToNotify() {
            return mWhenYouWantToNotify;
        }

        public void setWhenYouWantToNotify(int whenYouWantToNotify) {
            mWhenYouWantToNotify = whenYouWantToNotify;
        }

    }

    
}
