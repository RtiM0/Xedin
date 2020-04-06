package com.shakir.xedin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.R;
import com.shakir.xedin.adapters.MoviesAdapter;
import com.shakir.xedin.models.Movie;
import com.shakir.xedin.utils.MakeRestRequest;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class PopularTV extends Fragment {

    public PopularTV() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_popular_tv, container, false);
        MoviesAdapter mAdapter = new MoviesAdapter(getContext());
        RecyclerView mRecyclerView = v.findViewById(R.id.tvRecycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(new AlphaInAnimationAdapter(new ScaleInAnimationAdapter(mAdapter)));
        mRecyclerView.getLayoutManager();
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }
        mAdapter.setMovieList(movies);
        MakeRestRequest makeRestRequest = new MakeRestRequest("TV", mAdapter);
        makeRestRequest.makeRequest();
        return v;
    }
}
