package com.shakir.xedin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class Search extends Fragment {
    private int mode = 0;

    public Search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        MoviesAdapter mAdapter = new MoviesAdapter(getContext());
        RecyclerView mRecyclerView = v.findViewById(R.id.searchRecycler);
        EditText searcher = v.findViewById(R.id.searcher);
        Button switcher = v.findViewById(R.id.switcher);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(new AlphaInAnimationAdapter(new ScaleInAnimationAdapter(mAdapter)));
        mRecyclerView.getLayoutManager();
        mRecyclerView.setVisibility(View.GONE);
        switcher.setOnClickListener(v1 -> {
            if (mode == 0) {
                switcher.setText("Movies");
                searcher.setHint("TV shows");
                mode = 1;
            } else {
                switcher.setText("TV");
                searcher.setHint("Movies");
                mode = 0;
            }
        });
        searcher.setOnClickListener(v12 -> {
            String query = searcher.getText().toString();
            MakeRestRequest makeRestRequest;
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                movies.add(new Movie());
            }
            mAdapter.setMovieList(movies);
            if (mode == 0) {
                makeRestRequest = new MakeRestRequest("Movie", query, mAdapter);
            } else {
                makeRestRequest = new MakeRestRequest("TV", mAdapter);
            }
            makeRestRequest.makeRequest();
            mRecyclerView.setVisibility(View.VISIBLE);
        });
        return v;
    }
}
