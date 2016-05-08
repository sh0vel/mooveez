package com.app.shovonh.mooveez;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMoviesFragment extends Fragment {

    public static final String PAGE_NUM = "page_number";
    public static int pageNumber = 0;


    public static ImageAdapter _gridAdapter;
    public static ArrayList<MovieObj> _newMoviesList;
    public static String MOVIE_DETAILS_BUNDLE_ID = "movedetails";

    public NewMoviesFragment() {
        // Required empty public constructor
    }

    public NewMoviesFragment newInstance(int page_number){
        NewMoviesFragment fragment = new NewMoviesFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUM, page_number);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(PAGE_NUM);
        if (FetchMoviesTask._popularMovies.isEmpty()){
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute(FetchMoviesTask.FETCH_MOST_POPULAR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);

        _newMoviesList = new ArrayList<>();
        _newMoviesList = FetchMoviesTask._popularMovies;
        _gridAdapter = new ImageAdapter(getActivity(), _newMoviesList);




        return view;
    }


}
