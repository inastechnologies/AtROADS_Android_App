
package com.inas.atroads.views.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EndRideResponseModel {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("result")
        @Expose
        private List<Result> result = null;
        @SerializedName("status")
        @Expose
        private Integer status;

        public String getMessage() {
        return message;
        }

        public void setMessage(String message) {
        this.message = message;
        }

        public List<Result> getResult() {
        return result;
         }

        public void setResult(List<Result> result) {
        this.result = result;
         }

        public Integer getStatus() {
        return status;
         }

        public void setStatus(Integer status) {
        this.status = status;
         }

    public class Result {

        @SerializedName("auto_number")
        @Expose
        private String autoNumber;
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("user_id")
        @Expose
        private Integer userId;

        public String getAutoNumber() {
            return autoNumber;
        }

        public void setAutoNumber(String autoNumber) {
            this.autoNumber = autoNumber;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

    }


}
