package com.inas.atroads.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bounds {
    @SerializedName("northeast")
    @Expose
    private AddressComponentsResponse.Result.Geometry.Bounds.Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private AddressComponentsResponse.Result.Geometry.Bounds.Southwest southwest;

    public AddressComponentsResponse.Result.Geometry.Bounds.Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(AddressComponentsResponse.Result.Geometry.Bounds.Northeast northeast) {
        this.northeast = northeast;
    }

    public AddressComponentsResponse.Result.Geometry.Bounds.Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(AddressComponentsResponse.Result.Geometry.Bounds.Southwest southwest) {
        this.southwest = southwest;
    }
}
