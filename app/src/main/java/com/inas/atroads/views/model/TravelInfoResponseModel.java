package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelInfoResponseModel {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("status")
    @Expose
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public class Result {

        @SerializedName("droping")
        @Expose
        private String droping;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("pickup")
        @Expose
        private String pickup;
        @SerializedName("sharing")
        @Expose
        private Integer sharing;
        @SerializedName("vehical")
        @Expose
        private String vehical;

        public String getDroping() {
            return droping;
        }

        public void setDroping(String droping) {
            this.droping = droping;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPickup() {
            return pickup;
        }

        public void setPickup(String pickup) {
            this.pickup = pickup;
        }

        public Integer getSharing() {
            return sharing;
        }

        public void setSharing(Integer sharing) {
            this.sharing = sharing;
        }

        public String getVehical() {
            return vehical;
        }

        public void setVehical(String vehical) {
            this.vehical = vehical;
        }

    }
}
