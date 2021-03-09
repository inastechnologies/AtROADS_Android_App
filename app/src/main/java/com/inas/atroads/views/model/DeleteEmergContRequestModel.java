
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DeleteEmergContRequestModel {

    @SerializedName("sos_id")
    private int msos_id;
    @SerializedName("user_id")
    private int mUserId;

    public int getsos_id() {
        return msos_id;
    }

    public void setsos_id(int sos_id) {
        msos_id = sos_id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
