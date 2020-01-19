package com.example.volleybasicexample.Model;

public class ModelFilm {
    private String title, thumbnailURL, overView, status, genres, rating, releaseDate;

    public ModelFilm(){

    }

    public ModelFilm(String title, String thumbnailURL, String overView, String status, String genres, String rating, String releaseDate) {
        this.title = title; //
        this.thumbnailURL = thumbnailURL;//
        this.overView = overView;
        this.status = status;
        this.genres = genres; //
        this.rating = rating; //
        this.releaseDate = releaseDate;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
