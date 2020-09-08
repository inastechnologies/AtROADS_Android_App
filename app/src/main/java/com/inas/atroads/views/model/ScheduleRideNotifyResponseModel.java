
package com.inas.atroads.views.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ScheduleRideNotifyResponseModel {

    @SerializedName("message")
    private String mMessage;
//    @SerializedName("result")
//    private List<Result> mResult;
    @SerializedName("status")
    private int mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

//    public List<Result> getResult() {
//        return mResult;
//    }
//
//    public void setResult(List<Result> result) {
//        mResult = result;
//    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

//
//    public class Result {
//
//        @SerializedName("res")
//        private String mRes;
//
//        public String getRes() {
//            return mRes;
//        }
//
//        public void setRes(String res) {
//            mRes = res;
//        }
//
//    }

}
