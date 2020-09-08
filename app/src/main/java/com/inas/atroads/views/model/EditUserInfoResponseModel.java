
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EditUserInfoResponseModel {

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
        @SerializedName("name")
        private String mName;
        @SerializedName("profile_pic")
        private String mProfilePic;
        @SerializedName("updated_time")
        private String mUpdatedTime;
        @SerializedName("user_id")
        private int mUserId;

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

        public String getUpdatedTime() {
            return mUpdatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            mUpdatedTime = updatedTime;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

    }


}
