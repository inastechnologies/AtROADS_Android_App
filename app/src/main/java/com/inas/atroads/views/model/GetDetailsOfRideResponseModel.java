
package com.inas.atroads.views.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetDetailsOfRideResponseModel {

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

        @SerializedName("auto_number")
        private String mAutoNumber;
        @SerializedName("dest")
        private String mDest;
        @SerializedName("fullname")
        private String mFullname;
        @SerializedName("source")
        private String mSource;
        @SerializedName("user_ride_id")
        private int user_ride_id;

        public String getAutoNumber() {
            return mAutoNumber;
        }

        public void setAutoNumber(String autoNumber) {
            mAutoNumber = autoNumber;
        }

        public String getDest() {
            return mDest;
        }

        public void setDest(String dest) {
            mDest = dest;
        }

        public String getFullname() {
            return mFullname;
        }

        public void setFullname(String fullname) {
            mFullname = fullname;
        }

        public String getSource() {
            return mSource;
        }

        public void setSource(String source) {
            mSource = source;
        }

        public int getUser_ride_id() {
            return user_ride_id;
        }

        public void setUser_ride_id(int user_ride_id) {
            this.user_ride_id = user_ride_id;
        }
    }

}
