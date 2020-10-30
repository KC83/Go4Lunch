package com.kcapp.go4lunch.model;

import androidx.annotation.Nullable;

import com.kcapp.go4lunch.api.services.Constants;

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
    @Nullable
    private String sendNotification;

    public User() {}

    public User(String uid, String username, @Nullable String email, @Nullable String urlPicture, @Nullable String placeId, @Nullable String placeDate, @Nullable String sendNotification) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.placeId = placeId;
        this.placeDate = placeDate;

        if (sendNotification == null) {
            sendNotification = Constants.SEND_NOTIFICATION_TRUE;
        }
        this.sendNotification = sendNotification;
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
    @Nullable
    public String getSendNotification() {
        return sendNotification;
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
    public void setSendNotification(@Nullable String sendNotification) {
        this.sendNotification = sendNotification;
    }
}
