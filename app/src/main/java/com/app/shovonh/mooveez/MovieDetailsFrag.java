package com.app.shovonh.mooveez;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.Objs.Trailer;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;


public class MovieDetailsFrag extends Fragment {
    public static final String LOG_TAG = MovieDetailsFrag.class.getSimpleName();

    private static final String SELECTED_MOVIE_BUNDLE_ID = "param1";

    // TODO: Rename and change types of parameters
    //array 0:poster 1:title 2:release 3:rating 4:description
    MovieObj movie;


    private static ImageView imgPoster, imgBackdrop;
    private static TextView tvTitle, tvRelease, tvRating, tvDescription, tvTrailer1, tvTrailer2, tvGenres;

    public MovieDetailsFrag() {
        // Required empty public constructor
    }

    static void initializeView(View view) {

        tvTitle = (TextView) view.findViewById(R.id.movie_title);
        tvRelease = (TextView) view.findViewById(R.id.release);
        tvDescription = (TextView) view.findViewById(R.id.description);
        imgBackdrop = (ImageView) view.findViewById(R.id.backdrop);
        tvGenres = (TextView) view.findViewById(R.id.genres);
    }


    public static MovieDetailsFrag newInstance(MovieObj movieObj) {

        MovieDetailsFrag fragment = new MovieDetailsFrag();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_MOVIE_BUNDLE_ID, Parcels.wrap(movieObj));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = Parcels.unwrap(getArguments().getParcelable(SELECTED_MOVIE_BUNDLE_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(com.app.shovonh.mooveez.R.layout.fragment_movie_details, container, false);
        initializeView(view);
        Picasso.with(getContext()).load(movie.getBackdrop()).into(imgBackdrop);


        tvTitle.setText(movie.getTitle());
        tvRelease.setText(Utilities.dateFormatter(movie.getReleaseDate()));
        tvDescription.setText(movie.getDescription());
        tvGenres.setText(Utilities.genresToString(movie.getGenres()));

        LinearLayout linearList = (LinearLayout) view.findViewById(R.id.add_list_items_here);
        for(int i = 0; i < movie.getTrailers().length; i++){
            View trailerView = inflater.inflate(R.layout.trailers_list_item, null);
            Button b = (Button) trailerView.findViewById(R.id.trailer_list_item_text);
            Trailer tr = movie.getTrailer(i);
            final String link = tr.getLink();
            b.setText(tr.getName());
            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                }
            });
            linearList.addView(trailerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return view;
    }

//        @Override
//        protected void onPostExecute(Trailer[] trailerArray) {
//
//            super.onPostExecute(trailerArray);
//            FetchUSDate fetchUSDate = new FetchUSDate();
//            //fetchUSDate.execute(movieDetails[6], movieDetails[2]);
//
//            for (Trailer t : trailerArray) {
//                trailers.add(t);
//            }
//            for (int i = 0; i < trailers.size(); i++) {
//                View v = layoutInflater.inflate(R.layout.trailers_list_item, null);
//                //TextView tv = (TextView) v.findViewById(R.id.trailer_list_item_text);
//                Button b = (Button) v.findViewById(R.id.trailer_list_item_text);
//                Trailer tr = trailers.get(i);
//                final String link = tr.getLink();
//                //tv.setText(tr.name);
//                b.setText(tr.getName());
//                b.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                        context.startActivity(browserIntent);
//                    }
//                });
//
//                linearList.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            }
//        }
    }

