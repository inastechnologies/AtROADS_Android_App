package com.inas.atroads.views.model;

public class SuggestionsListModel
{
    String name;
    String gender;
    int pstatus;
    String srcAddress;
    String DestAddress;
    int PairableUserID;

    public SuggestionsListModel(String name, String gender, int pstatus, String srcAddress, String destAddress, int pairableUserID) {
        this.name = name;
        this.gender = gender;
        this.pstatus = pstatus;
        this.srcAddress = srcAddress;
        DestAddress = destAddress;
        PairableUserID = pairableUserID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPstatus() {
        return pstatus;
    }

    public void setPstatus(int pstatus) {
        this.pstatus = pstatus;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getDestAddress() {
        return DestAddress;
    }

    public void setDestAddress(String destAddress) {
        DestAddress = destAddress;
    }

    public int getPairableUserID() {
        return PairableUserID;
    }

    public void setPairableUserID(int pairableUserID) {
        PairableUserID = pairableUserID;
    }
}
