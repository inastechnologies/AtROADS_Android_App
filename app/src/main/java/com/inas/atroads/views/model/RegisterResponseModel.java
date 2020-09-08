
package com.inas.atroads.views.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RegisterResponseModel {

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

        @SerializedName("confirm_password")
        private String mConfirmPassword;
        @SerializedName("created_time")
        private String mCreatedTime;
        @SerializedName("email_id")
        private String mEmailId;
        @SerializedName("fullname")
        private String mFullname;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("OTP")
        private String mOTP;
        @SerializedName("password")
        private String mPassword;
        @SerializedName("referralcode")
        private String mReferralcode;
        @SerializedName("user_id")
        private int mUserId;

        public String getConfirmPassword() {
            return mConfirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            mConfirmPassword = confirmPassword;
        }

        public String getCreatedTime() {
            return mCreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime = createdTime;
        }

        public String getEmailId() {
            return mEmailId;
        }

        public void setEmailId(String emailId) {
            mEmailId = emailId;
        }

        public String getFullname() {
            return mFullname;
        }

        public void setFullname(String fullname) {
            mFullname = fullname;
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

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        public String getReferralcode() {
            return mReferralcode;
        }

        public void setReferralcode(String referralcode) {
            mReferralcode = referralcode;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

    }


}
