package com.shakir.xedin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.adapters.MoviesAdapter;
import com.shakir.xedin.interfaces.MoviesApiService;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoviesApiService service = retrofit.create(MoviesApiService.class);
        service.getPopularShows(BuildConfig.API_KEY).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieResult = response.body();
                mAdapter.setMovieList(movieResult.getMovies());
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
        return v;
    }
}
