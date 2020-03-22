package com.shakir.xedin.interfaces;

import com.shakir.xedin.utils.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApiService {
    @GET("movie/popular")
    Call<MovieResult> getPopularMovies(@Query("api_key") String apiKey);

    @GET("tv/popular")
    Call<MovieResult> getPopularShows(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResult> getSearchMovies(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("search/tv")
    Call<MovieResult> getSearchShows(@Query("api_key") String apiKey, @Query("query") String query);
}

