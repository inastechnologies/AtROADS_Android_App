
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetUserInfoResponseModel {

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

        @SerializedName("accepting_other_gender")
        private String mAcceptingOtherGender;
        @SerializedName("email_id")
        private String mEmailId;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("name")
        private String mName;
        @SerializedName("profile_pic")
        private String mProfilePic;
        @SerializedName("user_id")
        private Long mUserId;

        public String getAcceptingOtherGender() {
            return mAcceptingOtherGender;
        }

        public void setAcceptingOtherGender(String acceptingOtherGender) {
            mAcceptingOtherGender = acceptingOtherGender;
        }

        public String getEmailId() {
            return mEmailId;
        }

        public void setEmailId(String emailId) {
            mEmailId = emailId;
        }

        public String getMobileNumber() {
            return mMobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            mMobileNumber = mobileNumber;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getProfilePic() {
            return mProfilePic;
        }

        public void setProfilePic(String profilePic) {
            mProfilePic = profilePic;
        }

        public Long getUserId() {
            return mUserId;
        }

        public void setUserId(Long userId) {
            mUserId = userId;
        }

    }

    
}
