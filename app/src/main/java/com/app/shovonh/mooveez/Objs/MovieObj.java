package com.app.shovonh.mooveez.Objs;

import org.parceler.Parcel;

import java.util.Arrays;

/**
 * Created by Shovon on 4/26/16.
 */
@Parcel
public class MovieObj {
    String title, description, releaseDate, rating, cover, backdrop;
    int[] genres;
    int id, releaseType;
    Trailer[] trailers;
    Cast[] castMembers;

    public MovieObj(){

    }



    public MovieObj(String title, String description, String releaseDate, String cover, String backdrop, int[] genres, int id) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.cover = "http://image.tmdb.org/t/p/w500/" + cover;
        this.backdrop = "http://image.tmdb.org/t/p/w500/" + backdrop;
        this.genres = new int[genres.length];
        this.genres = Arrays.copyOf(genres, genres.length);
        this.id = id;

    }

    public MovieObj(String title, String release, int id) {
        this.title = title;
        this.releaseDate = release;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
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

    public int getId() {
        return id;
    }

    public void setReleaseDate(String newDate) {
        this.releaseDate = newDate;
    }

    public Trailer[] getTrailers() {
        return trailers;
    }

    public Trailer getTrailer(int i){
        return trailers[i];
    }

    public Cast[] getCastMembers() {
        return castMembers;
    }

    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
    }

    public void setCastMembers(Cast[] castMembers) {
        this.castMembers = castMembers;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }

}
