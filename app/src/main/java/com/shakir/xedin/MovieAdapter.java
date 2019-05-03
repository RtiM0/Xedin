package com.shakir.xedin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {

    public static final String MOVIE_BASE_URL="https://image.tmdb.org/t/p/w185";

    private Context mContext;
    ArrayList<Movie> list;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.mContext = context;
        this.list = movieList;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Movie getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Movie movies = getItem(position);
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(200, 300));
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            relativeLayout.addView(imageView);
        } else {
            imageView = (ImageView) convertView;
        }

        //load data into the ImageView using Picasso
        Picasso.get().load(MOVIE_BASE_URL + movies.getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);

        return imageView;
    }
}
