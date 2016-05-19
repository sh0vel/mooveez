package com.app.shovonh.mooveez;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shovon on 4/29/16.
 */
public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<MovieObj> movieList = new ArrayList<>();

    public ImageAdapter(Context c, ArrayList<MovieObj> m) {
        mContext = c;
        movieList = m;
    }

    public void setData(ArrayList<MovieObj> m)
    {
        movieList = m;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
           // imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }

        Picasso.with(mContext).load(movieList.get(i).getCover()).resize(0, 750).into(imageView);



        return imageView;
    }
}