package com.wrexsoft.canturgut.patide.user;

/**
 * Created by CanTurgut on 23/05/2017.
 */

class userData {
    private static final userData userInstance = new userData();

    private String username;
    private String email;
    private String userUID;

    static userData getInstance() {
        return userInstance;
    }
    private userData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
