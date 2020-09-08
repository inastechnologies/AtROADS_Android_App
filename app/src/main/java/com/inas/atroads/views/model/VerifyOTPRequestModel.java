
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VerifyOTPRequestModel {

    @SerializedName("mobile_number")
    private String mMobileNumber;
    @SerializedName("otp_entered")
    private String mOtpEntered;

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getOtpEntered() {
        return mOtpEntered;
    }

    public void setOtpEntered(String otpEntered) {
        mOtpEntered = otpEntered;
    }

}
