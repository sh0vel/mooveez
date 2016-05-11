package com.app.shovonh.mooveez;

import java.util.Arrays;

/**
 * Created by Shovon on 4/26/16.
 */
public class MovieObj extends Object {
    String title, description, releaseDate, rating, cover, backdrop;
    int [] genres;

    public MovieObj(String title, String description,String releaseDate, String rating, String cover, String backdrop){
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.cover = "http://image.tmdb.org/t/p/w500/"+cover;
        this.backdrop = "http://image.tmdb.org/t/p/w500/" + backdrop;
    }

    public MovieObj(String title, String description,String releaseDate,String cover, String backdrop, int [] genres){
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.cover = "http://image.tmdb.org/t/p/w500/"+cover;
        this.backdrop = "http://image.tmdb.org/t/p/w500/" + backdrop;
        this.genres = new int [genres.length];
        this.genres = Arrays.copyOf(genres, genres.length);
    }

}
