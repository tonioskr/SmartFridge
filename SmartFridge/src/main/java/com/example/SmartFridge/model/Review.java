package com.example.SmartFridge.model;

import java.util.Date;

public class Review {
    private String idUser;
    private String posts;
    private Date lastUpdate;

    public Review(String id, String post, Date data)
    {
        this.idUser= id;
        this.posts = post;
        this.lastUpdate= data;
    }
    public Date getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPosts() {
        return posts;
    }
    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getIdUser() {
        return idUser;
    }
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Post{" +
                "idUser=" + idUser +
                ", posts=" + posts +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
