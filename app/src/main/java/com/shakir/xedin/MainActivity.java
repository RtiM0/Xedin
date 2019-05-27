package com.shakir.xedin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    int flag = 1;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private LinearLayout search;
    private EditText searcher;
    private Button switcher;
    private int mode = 0;
    //ProgressBar mProgressBar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    search.setVisibility(View.GONE);
                    if (flag != 1) {
                        UseTheRest("movies");
                        flag = 1;
                    }
                    return true;
                case R.id.navigation_dashboard:
                    search.setVisibility(View.GONE);
                    if (flag != 0) {
                        UseTheRest("tv");
                        flag = 0;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (flag != 2) {
                        search.setVisibility(View.VISIBLE);
                        searcher.setText("");
                        mRecyclerView.setVisibility(View.GONE);
                        searcher.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UseTheRest("search");
                            }
                        });
                        flag = 2;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        searcher = findViewById(R.id.searcher);
        search = findViewById(R.id.search);
        switcher = findViewById(R.id.switcher);
        //mProgressBar = findViewById(R.id.indeterminateBar);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getLayoutManager();
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 0) {
                    switcher.setText("Movies");
                    searcher.setHint("TV shows");
                    mode = 1;
                } else {
                    switcher.setText("TV");
                    searcher.setHint("Movies");
                    mode = 0;
                }
            }
        });
        //.setMeasurementCacheEnabled(false);
        UseTheRest("movies");
        //mProgressBar.setVisibility(View.VISIBLE);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    void UseTheRest(String serv) {
        final String s = serv;
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }
        mAdapter.setMovieList(movies);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", BuildConfig.API_KEY);
                        if (s == "search") {
                            request.addEncodedQueryParam("query", searcher.getText().toString());
                        }
                    }
                })
                .build();
        MoviesApiService service = restAdapter.create(MoviesApiService.class);
        if (serv == "movies") {
            service.getPopularMovies(new Callback<MovieResult>() {
                @Override
                public void success(MovieResult movieResult, Response response) {
                    mAdapter.setMovieList(movieResult.getResults());
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        } else if (serv == "tv") {
            service.getPopularShows(new Callback<MovieResult>() {
                @Override
                public void success(MovieResult movieResult, Response response) {
                    mAdapter.setMovieList(movieResult.getResults());
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        } else if (serv == "search") {
            if (mode == 0) {
                service.getSearchMovies(new Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        mAdapter.setMovieList(movieResult.getResults());
                        mRecyclerView.setVisibility(View.VISIBLE);
                        search.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            } else {
                service.getSearchShows(new Callback<MovieResult>() {
                    @Override
                    public void success(MovieResult movieResult, Response response) {
                        mAdapter.setMovieList(movieResult.getResults());
                        mRecyclerView.setVisibility(View.VISIBLE);
                        search.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
        }
    }
}
