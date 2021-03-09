
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SMSRequestModel {

    @SerializedName("mobile_number")
    private String mobilenumber;
    @SerializedName("message")
    private String message;


    public String getmobilenumber() {
        return mobilenumber;
    }

    public void setmobilenumber(String Mmobilenumber) {
        mobilenumber = Mmobilenumber;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String Mmessage) {
        message = Mmessage;
    }


}
