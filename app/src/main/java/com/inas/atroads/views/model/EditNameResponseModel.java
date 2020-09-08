
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EditNameResponseModel {

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

        @SerializedName("fullname")
        private String mFullname;
        @SerializedName("user_id")
        private Long mUserId;

        public String getFullname() {
            return mFullname;
        }

        public void setFullname(String fullname) {
            mFullname = fullname;
        }

        public Long getUserId() {
            return mUserId;
        }

        public void setUserId(Long userId) {
            mUserId = userId;
        }

    }


}
