
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MeterCalculationResponeModel {

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
        @SerializedName("payable_amount")
        private Double mPayableAmount;
        @SerializedName("total_fare")
        private Long mTotalFare;
        @SerializedName("user_id")
        private int mUserId;
        @SerializedName("user_ride_id")
        private int mUserRideId;
        @SerializedName("id")
        private int IdToGetYourBill;
        @SerializedName("distance")
        private Double mDistance;

        public Double getDistance() {
            return mDistance;
        }

        public void setDistance(Double distance) {
            mDistance = distance;
        }


        public String getAutoNumber() {
            return mAutoNumber;
        }

        public void setAutoNumber(String autoNumber) {
            mAutoNumber = autoNumber;
        }

        public Double getPayableAmount() {
            return mPayableAmount;
        }

        public void setPayableAmount(Double payableAmount) {
            mPayableAmount = payableAmount;
        }

        public Long getTotalFare() {
            return mTotalFare;
        }

        public void setTotalFare(Long totalFare) {
            mTotalFare = totalFare;
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

        public int getIdToGetYourBill() {
            return IdToGetYourBill;
        }

        public void setIdToGetYourBill(int idToGetYourBill) {
            IdToGetYourBill = idToGetYourBill;
        }


    }



}
