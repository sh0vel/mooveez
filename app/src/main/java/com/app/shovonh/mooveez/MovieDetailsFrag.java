package com.app.shovonh.mooveez;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailsFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFrag extends Fragment {
    public static final String LOG_TAG = MovieDetailsFrag.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MOVIE_DETAILS_ARRAY = "param1";

    // TODO: Rename and change types of parameters
    //array 0:poster 1:title 2:release 3:rating 4:description 5:trailer1 6:trailer2
    private String movieDetails [];


    private OnFragmentInteractionListener mListener;

    private static ImageView imgPoster;
    private static TextView tvTitle, tvRelease, tvRating, tvDescription, tvTrailer1, tvTrailer2;

    public MovieDetailsFrag() {
        // Required empty public constructor
    }

    static void initializeView(View view){
        imgPoster = (ImageView) view.findViewById(com.app.shovonh.mooveez.R.id.poster);
        tvTitle = (TextView) view.findViewById(com.app.shovonh.mooveez.R.id.title);
        tvRelease = (TextView) view.findViewById(com.app.shovonh.mooveez.R.id.release_date);
        tvRating = (TextView) view.findViewById(com.app.shovonh.mooveez.R.id.rating);
        tvDescription = (TextView) view.findViewById(com.app.shovonh.mooveez.R.id.description);
        tvTrailer1 = (TextView) view.findViewById(com.app.shovonh.mooveez.R.id.trailer1);
        tvTrailer2 = (TextView) view.findViewById(com.app.shovonh.mooveez.R.id.trailer2);
    }


    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFrag newInstance(String movieDetails[]) {

        MovieDetailsFrag fragment = new MovieDetailsFrag();
        Bundle args = new Bundle();
        args.putStringArray(MOVIE_DETAILS_ARRAY, movieDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieDetails = getArguments().getStringArray(MOVIE_DETAILS_ARRAY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.app.shovonh.mooveez.R.layout.fragment_movie_details, container, false);
        initializeView(view);
        Picasso.with(getContext()).load(movieDetails[0]).into(imgPoster);
        tvTitle.setText(movieDetails[1]);
        tvRelease.setText(movieDetails[2]);
        tvRating.setText(movieDetails[3]);
        tvDescription.setText(movieDetails[4]);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
