
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class StartRideForPairedUsersResponseModel {

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

        @SerializedName("start_time")
        private String mStartTime;
        @SerializedName("user_id")
        private int mUserId;
        @SerializedName("user_ride_id")
        private int mUserRideId;

        public String getStartTime() {
            return mStartTime;
        }

        public void setStartTime(String startTime) {
            mStartTime = startTime;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

        public int getUserRideId() {
            return mUserRideId;
        }

        public void setUserRideId(int userRideId) {
            mUserRideId = userRideId;
        }

    }


}
