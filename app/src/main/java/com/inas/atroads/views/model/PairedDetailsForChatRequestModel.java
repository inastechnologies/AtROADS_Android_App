
package com.inas.atroads.views.model;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PairedDetailsForChatRequestModel {

    @SerializedName("user_id")
    private int mUserId;

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
