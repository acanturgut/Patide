package com.wrexsoft.canturgut.patide.user;

/**
 * Created by CanTurgut on 23/05/2017.
 */

class UserData {
    private static final UserData userInstance = new UserData();

    private String username;
    private String email;
    private String userUID;

    static UserData getInstance() {
        return userInstance;
    }
    private UserData() {
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
