package com.kcapp.go4lunch.model;

public class PlaceLike {
    private String uid;
    private String userUid;
    private String placeId;

    public PlaceLike() { }

    public PlaceLike(String uid, String userUid, String placeId) {
        this.uid = uid;
        this.userUid = userUid;
        this.placeId = placeId;
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
}
