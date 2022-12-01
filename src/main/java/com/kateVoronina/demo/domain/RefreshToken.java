package com.kateVoronina.demo.domain;

public class RefreshToken {

    private int id;
    private String login;
    private String token;

    public RefreshToken(int id, String login, String token) {
        this.id = id;
        this.login = login;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setUser(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
