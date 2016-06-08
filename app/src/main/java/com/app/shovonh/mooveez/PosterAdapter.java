package com.app.shovonh.mooveez;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.shovonh.mooveez.Objs.MovieObj;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shovon on 6/4/16.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {
    private ArrayList<MovieObj> mMovies;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView picture;
        public ProgressWheel wheel;
        public ViewHolder (final View itemView){
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.img_poster);
            wheel = (ProgressWheel) itemView.findViewById(R.id.progress_wheel_poster);

        }
    }


    public PosterAdapter(ArrayList<MovieObj> m, Context context){
        mMovies = m;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster_img, parent, false);
        // set the view's size, margins, paddings and layout parameters
        view.setPadding(0,8,0,8);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Callback loadedCallback = new Callback() {
            @Override
            public void onSuccess() {
                holder.wheel.stopSpinning();

            }

            @Override
            public void onError() {
            }
        };
        Picasso.with(mContext).load(mMovies.get(position).getCover()).resize(0, 750).into(holder.picture, loadedCallback);
    }

    @Override
    public int getItemCount() {
        return mMovies.size() ;
    }
}
