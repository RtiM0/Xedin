package com.shakir.xedin.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.adapters.MoviesAdapter;
import com.shakir.xedin.interfaces.MoviesApiService;
import com.shakir.xedin.models.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RestMovieParse {
    public void UseRest(MoviesAdapter moviesAdapter, RecyclerView recyclerView, String on) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }
        moviesAdapter.setMovieList(movies);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", BuildConfig.API_KEY);
                    }
                })
                .build();
        MoviesApiService service = restAdapter.create(MoviesApiService.class);
        switch (on) {
            case "movie":
                service.getPopularMovies(new Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        moviesAdapter.setMovieList(movieResult.getResults());
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            case "tv":
                service.getPopularShows(new Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        moviesAdapter.setMovieList(movieResult.getResults());
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
        }
    }

    public void UseRest(MoviesAdapter moviesAdapter, RecyclerView recyclerView, String on, String searchQuery) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", BuildConfig.API_KEY);
                        request.addEncodedQueryParam("query", searchQuery);
                    }
                })
                .build();
        MoviesApiService service = restAdapter.create(MoviesApiService.class);
        switch (on) {
            case "movie":
                service.getSearchMovies(new Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        moviesAdapter.setMovieList(movieResult.getResults());
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            case "tv":
                service.getPopularShows(new Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        moviesAdapter.setMovieList(movieResult.getResults());
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
        }
    }
}
