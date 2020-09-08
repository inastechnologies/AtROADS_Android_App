package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentGatewayRequestModel {
    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("amount")
    @Expose
    private Double amount;

    @SerializedName("order_id")
    @Expose
    private String orderId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
