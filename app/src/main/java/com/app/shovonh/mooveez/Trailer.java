package com.app.shovonh.mooveez;

/**
 * Created by Shovon on 5/14/16.
 */
public class Trailer{
    String name, link;
    public Trailer(String name, String link){
        this.name = name;
        this.link = "https://www.youtube.com/watch?v=" + link;
    }
}