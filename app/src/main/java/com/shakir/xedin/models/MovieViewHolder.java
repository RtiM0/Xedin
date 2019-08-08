package com.shakir.xedin.models;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}