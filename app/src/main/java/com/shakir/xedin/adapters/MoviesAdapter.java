package com.shakir.xedin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.R;
import com.shakir.xedin.activities.InfoPage;
import com.shakir.xedin.models.Movie;
import com.shakir.xedin.utils.MovieViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MoviesAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_movie, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        Movie movie = mMovieList.get(position);
        final String poster = movie.getPoster();
        final String title = movie.getTitle();
        final String backdrop = movie.getBackdrop();
        final String desc = movie.getDescription();
        final String tvname = movie.getName();
        final int tmdbid = movie.getId();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InfoPage.class);
                intent.putExtra("title", title);
                intent.putExtra("poster", poster);
                intent.putExtra("backdrop", backdrop);
                intent.putExtra("desc", desc);
                intent.putExtra("tmdbid", tmdbid);
                intent.putExtra("tvname", tvname);
                mContext.startActivity(intent);
            }
        });
        // This is how we use Picasso to load images from the internet.
        Picasso.get()
                .load(movie.getPoster())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }
}