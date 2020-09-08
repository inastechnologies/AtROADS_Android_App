
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class NotificationResponseModel {

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

        @SerializedName("message_body")
        private String mMessageBody;
        @SerializedName("message_title")
        private String mMessageTitle;
        @SerializedName("user_id")
        private int mUserId;

        public String getMessageBody() {
            return mMessageBody;
        }

        public void setMessageBody(String messageBody) {
            mMessageBody = messageBody;
        }

        public String getMessageTitle() {
            return mMessageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            mMessageTitle = messageTitle;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

    }


}
