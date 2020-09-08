
package com.inas.atroads.notifications;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class NotificationRequestModel {

    @SerializedName("registration_id")
    private String mRegistrationId;

    public String getRegistrationId() {
        return mRegistrationId;
    }

    public void setRegistrationId(String registrationId) {
        mRegistrationId = registrationId;
    }

}
