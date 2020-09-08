
package com.inas.atroads.views.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddressComponentsResponse {

    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("status")
    private String mStatus;

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public class Result {

        @SerializedName("address_components")
        private List<AddressComponent> mAddressComponents;
        @SerializedName("formatted_address")
        private String mFormattedAddress;
        @SerializedName("geometry")
        private Geometry mGeometry;
        @SerializedName("place_id")
        private String mPlaceId;
        @SerializedName("types")
        private List<String> mTypes;

        public List<AddressComponent> getAddressComponents() {
            return mAddressComponents;
        }

        public void setAddressComponents(List<AddressComponent> addressComponents) {
            mAddressComponents = addressComponents;
        }

        public String getFormattedAddress() {
            return mFormattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            mFormattedAddress = formattedAddress;
        }

        public Geometry getGeometry() {
            return mGeometry;
        }

        public void setGeometry(Geometry geometry) {
            mGeometry = geometry;
        }

        public String getPlaceId() {
            return mPlaceId;
        }

        public void setPlaceId(String placeId) {
            mPlaceId = placeId;
        }

        public List<String> getTypes() {
            return mTypes;
        }

        public void setTypes(List<String> types) {
            mTypes = types;
        }

        public class AddressComponent {

            @SerializedName("long_name")
            private String mLongName;
            @SerializedName("short_name")
            private String mShortName;
            @SerializedName("types")
            private List<String> mTypes;

            public String getLongName() {
                return mLongName;
            }

            public void setLongName(String longName) {
                mLongName = longName;
            }

            public String getShortName() {
                return mShortName;
            }

            public void setShortName(String shortName) {
                mShortName = shortName;
            }

            public List<String> getTypes() {
                return mTypes;
            }

            public void setTypes(List<String> types) {
                mTypes = types;
            }

        }

        public class Geometry {

            @SerializedName("bounds")
            private Bounds mBounds;
            @SerializedName("location")
            private Location mLocation;
            @SerializedName("location_type")
            private String mLocationType;
            @SerializedName("viewport")
            private Viewport mViewport;

            public Bounds getBounds() {
                return mBounds;
            }

            public void setBounds(Bounds bounds) {
                mBounds = bounds;
            }

            public Location getLocation() {
                return mLocation;
            }

            public void setLocation(Location location) {
                mLocation = location;
            }

            public String getLocationType() {
                return mLocationType;
            }

            public void setLocationType(String locationType) {
                mLocationType = locationType;
            }

            public Viewport getViewport() {
                return mViewport;
            }

            public void setViewport(Viewport viewport) {
                mViewport = viewport;
            }

            public class Bounds {

                @SerializedName("northeast")
                private Northeast mNortheast;
                @SerializedName("southwest")
                private Southwest mSouthwest;

                public Northeast getNortheast() {
                    return mNortheast;
                }

                public void setNortheast(Northeast northeast) {
                    mNortheast = northeast;
                }

                public Southwest getSouthwest() {
                    return mSouthwest;
                }

                public void setSouthwest(Southwest southwest) {
                    mSouthwest = southwest;
                }

                public class Northeast {

                    @SerializedName("lat")
                    private Double mLat;
                    @SerializedName("lng")
                    private Double mLng;

                    public Double getLat() {
                        return mLat;
                    }

                    public void setLat(Double lat) {
                        mLat = lat;
                    }

                    public Double getLng() {
                        return mLng;
                    }

                    public void setLng(Double lng) {
                        mLng = lng;
                    }

                }

                public class Southwest {

                    @SerializedName("lat")
                    private Double mLat;
                    @SerializedName("lng")
                    private Double mLng;

                    public Double getLat() {
                        return mLat;
                    }

                    public void setLat(Double lat) {
                        mLat = lat;
                    }

                    public Double getLng() {
                        return mLng;
                    }

                    public void setLng(Double lng) {
                        mLng = lng;
                    }

                }
            }

            public class Location {

                @SerializedName("lat")
                private Double mLat;
                @SerializedName("lng")
                private Double mLng;

                public Double getLat() {
                    return mLat;
                }

                public void setLat(Double lat) {
                    mLat = lat;
                }

                public Double getLng() {
                    return mLng;
                }

                public void setLng(Double lng) {
                    mLng = lng;
                }

            }

            public class Viewport {

                @SerializedName("northeast")
                private Northeast mNortheast;
                @SerializedName("southwest")
                private Southwest mSouthwest;

                public Northeast getNortheast() {
                    return mNortheast;
                }

                public void setNortheast(Northeast northeast) {
                    mNortheast = northeast;
                }

                public Southwest getSouthwest() {
                    return mSouthwest;
                }

                public void setSouthwest(Southwest southwest) {
                    mSouthwest = southwest;
                }

                public class Northeast {

                    @SerializedName("lat")
                    private Double mLat;
                    @SerializedName("lng")
                    private Double mLng;

                    public Double getLat() {
                        return mLat;
                    }

                    public void setLat(Double lat) {
                        mLat = lat;
                    }

                    public Double getLng() {
                        return mLng;
                    }

                    public void setLng(Double lng) {
                        mLng = lng;
                    }

                }


                public class Southwest {

                    @SerializedName("lat")
                    private Double mLat;
                    @SerializedName("lng")
                    private Double mLng;

                    public Double getLat() {
                        return mLat;
                    }

                    public void setLat(Double lat) {
                        mLat = lat;
                    }

                    public Double getLng() {
                        return mLng;
                    }

                    public void setLng(Double lng) {
                        mLng = lng;
                    }

                }
            }


        }


    }


}
