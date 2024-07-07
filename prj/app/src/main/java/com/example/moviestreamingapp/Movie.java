package com.example.moviestreamingapp;

import java.util.List;

public class Movie {
    private String movieId;
    private String title;
    private String category;
    private String release_date;
    private String trailer_url;
    private String description;
    private List<String> actors;
    private int likes;
    private List<String> languages;
    private List<String> subtitles;
    private List<String> quality;
    private String imageUrl;
    public Movie() {
    }

    public Movie(String movieId, String title, String category, String release_date, String trailer_url, String description, List<String> actors, int likes, List<String> languages, List<String> subtitles, List<String> quality, String imageUrl) {
        this.movieId = movieId;
        this.title = title;
        this.category = category;
        this.release_date = release_date;
        this.trailer_url = trailer_url;
        this.description = description;
        this.actors = actors;
        this.likes = likes;
        this.languages = languages;
        this.subtitles = subtitles;
        this.quality = quality;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

    public void setTrailer_url(String trailer_url) {
        this.trailer_url = trailer_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<String> subtitles) {
        this.subtitles = subtitles;
    }

    public List<String> getQuality() {
        return quality;
    }

    public void setQuality(List<String> quality) {
        this.quality = quality;
    }
}
