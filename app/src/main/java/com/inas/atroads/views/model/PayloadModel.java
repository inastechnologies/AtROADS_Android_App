package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayloadModel implements Serializable
{

    @SerializedName("other")
    @Expose
    private String other;
    @SerializedName("pairing")
    @Expose
    private Integer pairing;
    @SerializedName("office")
    @Expose
    private String office;
    @SerializedName("When_you_want_to_notify")
    @Expose
    private Integer whenYouWantToNotify;
    @SerializedName("to_time")
    @Expose
    private String toTime;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("from_time")
    @Expose
    private String fromTime;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("home")
    @Expose
    private String home;
    private final static long serialVersionUID = -4951914559830027067L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PayloadModel() {
    }

    /**
     *
     * @param date
     * @param whenYouWantToNotify
     * @param other
     * @param pairing
     * @param fromTime
     * @param office
     * @param type
     * @param toTime
     * @param home
     */
    public PayloadModel(String other, Integer pairing, String office, Integer whenYouWantToNotify, String toTime, String type, String fromTime, String date, String home) {
        super();
        this.other = other;
        this.pairing = pairing;
        this.office = office;
        this.whenYouWantToNotify = whenYouWantToNotify;
        this.toTime = toTime;
        this.type = type;
        this.fromTime = fromTime;
        this.date = date;
        this.home = home;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Integer getPairing() {
        return pairing;
    }

    public void setPairing(Integer pairing) {
        this.pairing = pairing;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Integer getWhenYouWantToNotify() {
        return whenYouWantToNotify;
    }

    public void setWhenYouWantToNotify(Integer whenYouWantToNotify) {
        this.whenYouWantToNotify = whenYouWantToNotify;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

}
