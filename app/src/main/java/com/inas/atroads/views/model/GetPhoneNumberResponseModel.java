
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetPhoneNumberResponseModel {

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

        @SerializedName("created_time")
        private String mCreatedTime;
        @SerializedName("device_token")
        private String mDeviceToken;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("OTP")
        private String mOTP;
        @SerializedName("user_id")
        private int mUserId;

        public String getCreatedTime() {
            return mCreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime = createdTime;
        }

        public String getDeviceToken() {
            return mDeviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            mDeviceToken = deviceToken;
        }

        public String getMobileNumber() {
            return mMobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            mMobileNumber = mobileNumber;
        }

        public String getOTP() {
            return mOTP;
        }

        public void setOTP(String oTP) {
            mOTP = oTP;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

    }


}
