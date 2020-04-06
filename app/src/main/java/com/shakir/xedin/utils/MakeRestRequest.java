package com.shakir.xedin.utils;

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.adapters.MoviesAdapter;
import com.shakir.xedin.interfaces.TMDBApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MakeRestRequest implements Callback<MovieResult> {
    private String service;
    private String query;
    private Boolean search;
    private MoviesAdapter mAdapter;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private TMDBApiService apiService = retrofit.create(TMDBApiService.class);

    public MakeRestRequest(String service, MoviesAdapter mAdapter) {
        this.service = service;
        this.mAdapter = mAdapter;
        search = false;
    }

    public MakeRestRequest(String service, String query, MoviesAdapter mAdapter) {
        this.service = service;
        this.mAdapter = mAdapter;
        search = true;
        this.query = query;
    }

    public void makeRequest() {
        switch (service) {
            case "Movie":
                if (search) {
                    apiService.getSearchMovies(BuildConfig.API_KEY, query).enqueue(this);
                } else {
                    apiService.getPopularMovies(BuildConfig.API_KEY).enqueue(this);
                }
                break;
            case "TV":
                if (search) {
                    apiService.getSearchShows(BuildConfig.API_KEY, query).enqueue(this);
                } else {
                    apiService.getPopularShows(BuildConfig.API_KEY).enqueue(this);
                }
                break;
        }
    }

    @Override
    public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
        MovieResult movieResult = response.body();
        mAdapter.setMovieList(movieResult.getMovies());
    }

    @Override
    public void onFailure(Call<MovieResult> call, Throwable t) {
        call.clone().enqueue(this);
        t.printStackTrace();
    }
}
