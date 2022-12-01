package com.kateVoronina.demo.domain;

public class User {
    private Integer userId;
    private String login;
    private String hash;
    private String salt;

    public User(Integer userId, String login, String hash, String salt) {
        this.userId = userId;
        this.login = login;
        this.hash = hash;
        this.salt = salt;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }
}
