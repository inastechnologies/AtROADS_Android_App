
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EditNameRequestModel {

    @SerializedName("fullname")
    private String mFullname;
    @SerializedName("user_id")
    private int mUserId;

    public String getFullname() {
        return mFullname;
    }

    public void setFullname(String fullname) {
        mFullname = fullname;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
