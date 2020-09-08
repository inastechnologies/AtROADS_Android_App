
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UploadQRResponseModel {

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

        @SerializedName("qrcode")
        private String mQrcode;
        @SerializedName("updated_time")
        private String mUpdatedTime;
        @SerializedName("user_id")
        private int mUserId;

        public String getQrcode() {
            return mQrcode;
        }

        public void setQrcode(String qrcode) {
            mQrcode = qrcode;
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
