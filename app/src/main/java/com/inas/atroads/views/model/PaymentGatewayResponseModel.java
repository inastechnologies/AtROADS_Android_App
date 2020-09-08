package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentGatewayResponseModel {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("CALLBACK_URL")
        @Expose
        private String cALLBACKURL;
        @SerializedName("CHANNEL_ID")
        @Expose
        private String cHANNELID;
        @SerializedName("CHECKSUMHASH")
        @Expose
        private String cHECKSUMHASH;
        @SerializedName("CUST_ID")
        @Expose
        private String cUSTID;
        @SerializedName("INDUSTRY_TYPE_ID")
        @Expose
        private String iNDUSTRYTYPEID;
        @SerializedName("MID")
        @Expose
        private String mID;
        @SerializedName("ORDER_ID")
        @Expose
        private String oRDERID;
        @SerializedName("TXN_AMOUNT")
        @Expose
        private String tXNAMOUNT;
        @SerializedName("WEBSITE")
        @Expose
        private String wEBSITE;

        public String getCALLBACKURL() {
            return cALLBACKURL;
        }

        public void setCALLBACKURL(String cALLBACKURL) {
            this.cALLBACKURL = cALLBACKURL;
        }

        public String getCHANNELID() {
            return cHANNELID;
        }

        public void setCHANNELID(String cHANNELID) {
            this.cHANNELID = cHANNELID;
        }

        public String getCHECKSUMHASH() {
            return cHECKSUMHASH;
        }

        public void setCHECKSUMHASH(String cHECKSUMHASH) {
            this.cHECKSUMHASH = cHECKSUMHASH;
        }

        public String getCUSTID() {
            return cUSTID;
        }

        public void setCUSTID(String cUSTID) {
            this.cUSTID = cUSTID;
        }

        public String getINDUSTRYTYPEID() {
            return iNDUSTRYTYPEID;
        }

        public void setINDUSTRYTYPEID(String iNDUSTRYTYPEID) {
            this.iNDUSTRYTYPEID = iNDUSTRYTYPEID;
        }

        public String getMID() {
            return mID;
        }

        public void setMID(String mID) {
            this.mID = mID;
        }

        public String getORDERID() {
            return oRDERID;
        }

        public void setORDERID(String oRDERID) {
            this.oRDERID = oRDERID;
        }

        public String getTXNAMOUNT() {
            return tXNAMOUNT;
        }

        public void setTXNAMOUNT(String tXNAMOUNT) {
            this.tXNAMOUNT = tXNAMOUNT;
        }

        public String getWEBSITE() {
            return wEBSITE;
        }

        public void setWEBSITE(String wEBSITE) {
            this.wEBSITE = wEBSITE;
        }

    }


}