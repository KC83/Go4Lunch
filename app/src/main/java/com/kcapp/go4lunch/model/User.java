package com.kcapp.go4lunch.model;

import androidx.annotation.Nullable;

public class User {
    private String uid;
    private String username;
    @Nullable
    private String email;
    @Nullable
    private String urlPicture;
    @Nullable
    private String placeId;
    @Nullable
    private String placeDate;

    public User() { }

    public User(String uid, String username, @Nullable String email, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    public User(String uid, String username, @Nullable String email, @Nullable String urlPicture, @Nullable String placeId, @Nullable String placeDate) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.placeId = placeId;
        this.placeDate = placeDate;
    }

    /*** GETTERS ***/
    public String getUid() {
        return uid;
    }
    public String getUsername() {
        return username;
    }
    @Nullable
    public String getEmail() {
        return email;
    }
    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }
    @Nullable
    public String getPlaceId() {
        return placeId;
    }
    @Nullable
    public String getPlaceDate() {
        return placeDate;
    }

    /*** SETTERS ***/
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(@Nullable String email) {
        this.email = email;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
    public void setPlaceId(@Nullable String placeId) {
        this.placeId = placeId;
    }
    public void setPlaceDate(@Nullable String placeDate) {
        this.placeDate = placeDate;
    }
}
