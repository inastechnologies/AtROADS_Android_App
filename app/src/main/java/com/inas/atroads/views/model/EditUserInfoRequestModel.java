
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EditUserInfoRequestModel {

    @SerializedName("accepting_other_gender")
    private String mAcceptingOtherGender;
    @SerializedName("email_id")
    private String mEmailId;
    @SerializedName("name")
    private String mName;
    @SerializedName("profile_pic")
    private String mProfilePic;
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

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
