
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SendRequestModel {

    @SerializedName("pair_id")
    private int mPairId;
    @SerializedName("user_id")
    private int mUserId;

    public int getPairId() {
        return mPairId;
    }

    public void setPairId(int pairId) {
        mPairId = pairId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
