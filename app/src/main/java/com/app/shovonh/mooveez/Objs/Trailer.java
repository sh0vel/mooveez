package com.app.shovonh.mooveez.Objs;

import org.parceler.Parcel;

/**
 * Created by Shovon on 5/14/16.
 */
@Parcel
public class Trailer {
    String name, link;

    public Trailer(){

    }

    public Trailer(String name, String link) {
        this.name = name;
        this.link = "https://www.youtube.com/watch?v=" + link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}