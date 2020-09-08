
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SendRequestResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("rstatus")
    private boolean mRstatus;
    @SerializedName("status")
    private int mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean getRstatus() {
        return mRstatus;
    }

    public void setRstatus(boolean rstatus) {
        mRstatus = rstatus;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

}
