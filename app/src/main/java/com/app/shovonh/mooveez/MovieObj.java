package com.app.shovonh.mooveez;

/**
 * Created by Shovon on 4/26/16.
 */
public class MovieObj extends Object {
    String title, description, releaseDate, rating, cover;
    String [] movieDetails = new String[5];

    public MovieObj(String title, String description,String releaseDate, String rating, String cover){
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.cover = "http://image.tmdb.org/t/p/w500/"+cover;
    }

}
