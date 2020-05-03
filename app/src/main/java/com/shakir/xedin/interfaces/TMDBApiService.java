package com.shakir.xedin.interfaces;

import com.shakir.xedin.models.MediaDetail;
import com.shakir.xedin.models.TPBGET;
import com.shakir.xedin.models.YTSGET;
import com.shakir.xedin.utils.MovieResult;
import com.shakir.xedin.utils.TVSeason;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TMDBApiService {
    @GET("movie/popular")
    Call<MovieResult> getPopularMovies(@Query("api_key") String apiKey);

    @GET("tv/popular")
    Call<MovieResult> getPopularShows(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResult> getSearchMovies(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("search/tv")
    Call<MovieResult> getSearchShows(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("tv/{tv_id}/season/{season_number}")
    Call<TVSeason> getEpisodes(@Path("tv_id") int tvid,@Path("season_number") int season,@Query("api_key") String apiKey);

    @GET
    Call<MediaDetail> getDetail(@Url String url);

    @GET
    Call<YTSGET> getYTS(@Url String path);

    @GET
    Call<List<TPBGET>> getTPB(@Url String path);
}

