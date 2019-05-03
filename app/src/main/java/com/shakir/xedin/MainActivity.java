package com.shakir.xedin;

import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shakir.xedin.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.indeterminateBar)
    ProgressBar mProgressBar;
    String popularMoviesURL;
    String topRatedMoviesURL;
    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopTopRatedList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default
        new FetchMovies().execute();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            popularMoviesURL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+BuildConfig.API_KEY;
            topRatedMoviesURL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="+BuildConfig.API_KEY;

            mPopularList = new ArrayList<>();
            mTopTopRatedList = new ArrayList<>();
            try {
                if(NetworkUtils.networkStatus(MainActivity.this)){
                    mPopularList = NetworkUtils.fetchData(popularMoviesURL); //Get popular movies
                    mTopTopRatedList = NetworkUtils.fetchData(topRatedMoviesURL); //Get top rated movies
                }else{
                    Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
