package com.app.shovonh.mooveez;

import java.util.Arrays;

/**
 * Created by Shovon on 4/26/16.
 */
public class MovieObj extends Object {
   private String title, description, releaseDate, rating, cover, backdrop;
   private int [] genres;
    private int id;


    public MovieObj(String title, String description, String releaseDate, String rating, String cover, String backdrop){
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.cover = "http://image.tmdb.org/t/p/w500/"+cover;
        this.backdrop = "http://image.tmdb.org/t/p/w500/" + backdrop;
    }

    public MovieObj(String title, String description,String releaseDate,String cover, String backdrop, int [] genres, int id){
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.cover = "http://image.tmdb.org/t/p/w500/"+cover;
        this.backdrop = "http://image.tmdb.org/t/p/w500/" + backdrop;
        this.genres = new int [genres.length];
        this.genres = Arrays.copyOf(genres, genres.length);
        this.id = id;
    }

    public MovieObj(String title, String release, int id){
        this.title = title;
        this.releaseDate = release;
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public String getCover() {
        return cover;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public int[] getGenres() {
        return genres;
    }

    public int getId(){
        return id;
    }

    public void setReleaseDate(String newDate){
        this.releaseDate = newDate;
    }
}
