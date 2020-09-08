
package com.inas.atroads.views.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginResponseModel {

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
        @SerializedName("email_id")
        private String mEmailId;
        @SerializedName("fullname")
        private String mFullname;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("profile_pic")
        private String mProfilePic;
        @SerializedName("user_id")
        private int mUserId;

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

        public String getProfilePic() {
            return mProfilePic;
        }

        public void setProfilePic(String profilePic) {
            mProfilePic = profilePic;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

    }



}
