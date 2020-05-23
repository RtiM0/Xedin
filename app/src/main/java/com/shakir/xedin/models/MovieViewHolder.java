package com.shakir.xedin.models;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public CardView cardView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        cardView = itemView.findViewById(R.id.posterCard);
    }
}