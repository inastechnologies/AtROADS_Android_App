
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class YourBillResponseModel {

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

        @SerializedName("amount")
        private Double mAmount;
        @SerializedName("completed_date")
        private String mCompletedDate;
        @SerializedName("id")
        private int mId;
        @SerializedName("rate_the_ride")
        private String mRateTheRide;
        @SerializedName("review")
        private String mReview;
        @SerializedName("user_dest_address")
        private String mUserDestAddress;
        @SerializedName("user_id")
        private int mUserId;
        @SerializedName("user_source_address")
        private String mUserSourceAddress;

        public Double getAmount() {
            return mAmount;
        }

        public void setAmount(Double amount) {
            mAmount = amount;
        }

        public String getCompletedDate() {
            return mCompletedDate;
        }

        public void setCompletedDate(String completedDate) {
            mCompletedDate = completedDate;
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getRateTheRide() {
            return mRateTheRide;
        }

        public void setRateTheRide(String rateTheRide) {
            mRateTheRide = rateTheRide;
        }

        public String getReview() {
            return mReview;
        }

        public void setReview(String review) {
            mReview = review;
        }

        public String getUserDestAddress() {
            return mUserDestAddress;
        }

        public void setUserDestAddress(String userDestAddress) {
            mUserDestAddress = userDestAddress;
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

    }



}
