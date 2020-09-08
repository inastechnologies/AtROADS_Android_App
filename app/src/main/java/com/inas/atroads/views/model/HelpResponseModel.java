
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HelpResponseModel {

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

        @SerializedName("attachment")
        private String mAttachment;
        @SerializedName("description")
        private String mDescription;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("updated_time")
        private String mUpdatedTime;
        @SerializedName("user_id")
        private Long mUserId;

        public String getAttachment() {
            return mAttachment;
        }

        public void setAttachment(String attachment) {
            mAttachment = attachment;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getUpdatedTime() {
            return mUpdatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            mUpdatedTime = updatedTime;
        }

        public Long getUserId() {
            return mUserId;
        }

        public void setUserId(Long userId) {
            mUserId = userId;
        }

    }



}
