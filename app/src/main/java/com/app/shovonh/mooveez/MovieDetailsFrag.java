package com.app.shovonh.mooveez;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shovonh.mooveez.Objs.Cast;
import com.app.shovonh.mooveez.Objs.MovieObj;
import com.app.shovonh.mooveez.Objs.Trailer;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;


public class MovieDetailsFrag extends Fragment {
    public static final String LOG_TAG = MovieDetailsFrag.class.getSimpleName();

    private static final String SELECTED_MOVIE_BUNDLE_ID = "param1";
    MovieObj movie;

    private static TextView tvRelease, tvDescription, noCast;


    public MovieDetailsFrag() {
        // Required empty public constructor
    }

    public static OnFragmentInteractionListener mListener;

    static void initializeView(View view) {


        tvRelease = (TextView) view.findViewById(R.id.release);
        tvDescription = (TextView) view.findViewById(R.id.description);
        noCast = (TextView) view.findViewById(R.id.no_cast_text);

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

        setUpActionBar(movie.getBackdrop(), movie.getTitle(), Utilities.genresToString(movie.getGenres()) );

        tvRelease.setText(Utilities.dateFormatter(movie.getReleaseDate()));
        tvDescription.setText(movie.getDescription());

        if (movie.getTrailers().length > 0) {
            view.findViewById(R.id.no_trailers_text).setVisibility(View.GONE);
            LinearLayout linearList = (LinearLayout) view.findViewById(R.id.add_trailer_items_here);
            for (int i = 0; i < movie.getTrailers().length; i++) {
                View trailerView = inflater.inflate(R.layout.trailers_list_item, null);
                Button b = (Button) trailerView.findViewById(R.id.trailer_list_item_text);
                Trailer tr = movie.getTrailer(i);
                final String link = tr.getLink();

                b.setText(tr.getName());
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent lightboxIntent = new Intent(view.getContext(), CustomLightboxActivity.class);
                        lightboxIntent.putExtra(CustomLightboxActivity.KEY_VIDEO_ID, link);
                        startActivity(lightboxIntent);
                    }
                });
                linearList.addView(trailerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }

        if (movie.getCastMembers().length > 0) {
            LinearLayout linearList = (LinearLayout) view.findViewById(R.id.add_cast_items_here);
            for (int i = 0; i < movie.getCastMembers().length; i++) {
                Cast cast = movie.getCastMembers()[i];
                if (!cast.getImg().equals("http://image.tmdb.org/t/p/w500/null")) {
                    if (noCast.getVisibility() == View.VISIBLE)
                        noCast.setVisibility(View.GONE);

                    View viewCast = inflater.inflate(R.layout.item_cast, null);
                    ImageView img = (ImageView) viewCast.findViewById(R.id.cast_img);
                    TextView name = (TextView) viewCast.findViewById(R.id.cast_name);
                    TextView charr = (TextView) viewCast.findViewById(R.id.cast_char);
                    final ProgressWheel wheel2 = (ProgressWheel) viewCast.findViewById(R.id.progress_wheel_cast);

                    final Callback loadedCallback = new Callback() {
                        @Override
                        public void onSuccess() {
                            wheel2.stopSpinning();

                        }

                        @Override
                        public void onError() {
                        }
                    };
                    img.setTag(loadedCallback);
                    Picasso.with(getContext()).load(cast.getImg()).into(img, loadedCallback);
                    name.setText(cast.getName());
                    charr.setText(cast.getCharacter());

                    linearList.addView(viewCast, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }

        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    void setUpActionBar(String imgURL, String title, String genres){
        mListener.setupActionBar(imgURL, title, genres);
    }


    public interface OnFragmentInteractionListener {
        void setupActionBar(String imgURL, String title, String subtitle);
    }


}

