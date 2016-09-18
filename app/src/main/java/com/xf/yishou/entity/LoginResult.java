package com.xf.yishou.entity;

/**
 * Created by xsp on 2016/9/18.
 */
public class LoginResult {
    private String type;
    private User user;

    public LoginResult(String type, User user) {
        super();
        this.type = type;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResult [type=" + type + ", user=" + user + "]";
    }
}
