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

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.adapters.MoviesAdapter;
import com.shakir.xedin.interfaces.TMDBApiService;
import com.shakir.xedin.models.Movie;
import com.shakir.xedin.utils.MovieResult;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                movies.add(new Movie());
            }
            mAdapter.setMovieList(movies);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TMDBApiService service = retrofit.create(TMDBApiService.class);
            if (mode == 0) {
                service.getSearchMovies(BuildConfig.API_KEY, searcher.getText().toString())
                        .enqueue(new Callback<MovieResult>() {
                            @Override
                            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                                MovieResult movieResult = response.body();
                                mAdapter.setMovieList(movieResult.getMovies());
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<MovieResult> call, Throwable t) {

                            }
                        });
            } else {
                service.getSearchShows(BuildConfig.API_KEY, searcher.getText().toString())
                        .enqueue(new Callback<MovieResult>() {
                            @Override
                            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                                MovieResult movieResult = response.body();
                                mAdapter.setMovieList(movieResult.getMovies());
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<MovieResult> call, Throwable t) {

                            }
                        });
            }
        });
        return v;
    }
}
