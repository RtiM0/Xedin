package com.shakir.xedin;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}