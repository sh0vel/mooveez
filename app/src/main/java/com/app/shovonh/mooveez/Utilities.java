package com.app.shovonh.mooveez;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Shovon on 5/11/16.
 */
public class Utilities {
    public static String genreDict(int a){
        switch (a){
            case 28: return "Action";
            case 12: return "Adventure";
            case 16: return "Animation";
            case 35: return "Comedy";
            case 80: return "Crime";
            case 99: return "Documentary";
            case 18: return "Drama";
            case 10751: return "Family";
            case 14: return "Fantasy";
            case 10769: return "Foreign";
            case 36: return "History";
            case 27: return "Horror";
            case 10402: return "Music";
            case 9648: return "Mystery";
            case 10749: return "Romance";
            case 878: return "Science Fiction";
            case 10770: return "TV Movie";
            case 53: return "Thriller";
            case 10752: return "War";
            case 37: return "Western";
            default: return "";

        }
    }

    public static String genresToString(int [] genres){
        String genreString = "- ";
        for (int g: genres) {
          genreString +=  genreDict(g);
            genreString += " - ";
        }

        return genreString;
    }

    private static String dateFormatHelpersHelper(String day){
        switch (day){
            case "1": case "21":case "31": return "st";
            case "2":case "22": return "nd";
            case "3":case "23": return "rd";
            default: return "th";

        }
   }


    private static String dateFormatHelper(String dayNum){
        String digit = "";
        if (dayNum.charAt(0) == '0'){
            digit += dayNum.charAt(1);
            return digit + dateFormatHelpersHelper(digit);
        }else return dayNum + dateFormatHelpersHelper(dayNum);

    }

    public static String dateFormatter(String releaseDate){
        DateTime today = DateTime.now(TimeZone.getDefault());
        DateTime date = new DateTime(releaseDate);
        if (date.format("YYYY-MM-DD").equals(today.format("YYYY-MM-DD"))){
            return "Releasing today!";
        }
        if (date.isInTheFuture(TimeZone.getDefault())){
            return "Releasing on the " + dateFormatHelper(releaseDate.substring(8));
        }
        else if (date.isInThePast(TimeZone.getDefault()) ) {
            return "Released on the " + dateFormatHelper(releaseDate.substring(8));
        }
        return null;
    }

    public static boolean alreadyReleased(String releaseDate){
        DateTime date = new DateTime(releaseDate);
        return date.isInThePast(TimeZone.getDefault());
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
