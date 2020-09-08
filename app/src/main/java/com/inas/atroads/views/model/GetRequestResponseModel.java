
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetRequestResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("rstatus")
    private Boolean mRstatus;
    @SerializedName("status")
    private int mStatus;

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

    public Boolean getRstatus() {
        return mRstatus;
    }

    public void setRstatus(Boolean rstatus) {
        mRstatus = rstatus;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public class Result {

        @SerializedName("accept")
        private int mAccept;
        @SerializedName("pair_id")
        private int mPairId;
        @SerializedName("pair_user_name")
        private String pair_user_name;

        public int getAccept() {
            return mAccept;
        }

        public void setAccept(int accept) {
            mAccept = accept;
        }

        public int getPairId() {
            return mPairId;
        }

        public void setPairId(int pairId) {
            mPairId = pairId;
        }

        public String getPair_user_name() {
            return pair_user_name;
        }

        public void setPair_user_name(String pair_user_name) {
            this.pair_user_name = pair_user_name;
        }
    }
    
}
