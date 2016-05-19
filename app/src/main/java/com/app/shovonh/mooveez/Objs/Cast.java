package com.app.shovonh.mooveez.Objs;

import org.parceler.Parcel;

/**
 * Created by Shovon on 5/19/16.
 */
@Parcel
public class Cast {
    int id;
    String name;
    String character;
    String img;

    public Cast(){

    }

    public Cast(int id, String name, String character, String img) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.img = "http://image.tmdb.org/t/p/w500/" + img;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getImg() {
        return img;
    }
}
