package com.kcapp.go4lunch.model;

public class PlaceLunch {
    private String uid;
    private String userUid;
    private String placeId;
    private String date;

    public PlaceLunch() { }

    public PlaceLunch(String uid, String userUid, String placeId, String date) {
        this.uid = uid;
        this.userUid = userUid;
        this.placeId = placeId;
        this.date = date;
    }

    /*** GETTERS ***/
    public String getUid() {
        return uid;
    }
    public String getUserUid() {
        return userUid;
    }
    public String getPlaceId() {
        return placeId;
    }
    public String getDate() {
        return date;
    }

    /*** SETTERS ***/
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
