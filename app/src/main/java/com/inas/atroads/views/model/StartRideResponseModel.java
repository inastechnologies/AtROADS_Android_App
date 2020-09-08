
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class StartRideResponseModel {

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

        @SerializedName("amount")
        private Long mAmount;
        @SerializedName("auto_number")
        private String mAutoNumber;
        @SerializedName("start_time")
        private String mStartTime;
        @SerializedName("type")
        private String mType;

        public Long getAmount() {
            return mAmount;
        }

        public void setAmount(Long amount) {
            mAmount = amount;
        }

        public String getAutoNumber() {
            return mAutoNumber;
        }

        public void setAutoNumber(String autoNumber) {
            mAutoNumber = autoNumber;
        }

        public String getStartTime() {
            return mStartTime;
        }

        public void setStartTime(String startTime) {
            mStartTime = startTime;
        }

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

    }



}
