
package com.inas.atroads.views.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HelpRequestModel {

    @SerializedName("attachment")
    private String mAttachment;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("user_id")
    private int mUserId;

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

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
