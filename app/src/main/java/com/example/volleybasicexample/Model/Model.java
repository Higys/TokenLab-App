package com.example.volleybasicexample.Model;

public class Model {
    private String title, thumbnailURL;
    private int id;

    public Model(){
    }

    public Model(String title, int id, String thumbnailURL){
        this.title = title;
        this.id = id;
        this.thumbnailURL = thumbnailURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
