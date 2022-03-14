package com.secondcommit.forum.security.payload;

/**
 * Dto used for the login requests
 */
public class LoginRequest {

    private String username;
    private String password;
    private String remember;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password, String remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }
}
