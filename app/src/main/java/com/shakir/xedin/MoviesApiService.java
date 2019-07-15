package com.shakir.xedin;

import retrofit.Callback;
import retrofit.http.GET;

public interface MoviesApiService {
    @GET("/movie/popular")
    void getPopularMovies(Callback<MovieResult> cb);

    @GET("/tv/popular")
    void getPopularShows(Callback<MovieResult> cb);

    @GET("/search/movie")
    void getSearchMovies(Callback<MovieResult> cb);

    @GET("/search/tv")
    void getSearchShows(Callback<MovieResult> cb);
}

