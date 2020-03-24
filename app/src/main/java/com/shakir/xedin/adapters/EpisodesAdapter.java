package com.shakir.xedin.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shakir.xedin.R;
import com.shakir.xedin.fragments.episodesFragment.OnListFragmentInteractionListener;
import com.shakir.xedin.models.TVEpisode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private final List<TVEpisode> episodes;
    private final OnListFragmentInteractionListener mListener;

    public EpisodesAdapter(OnListFragmentInteractionListener listener) {
        episodes = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_episodes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = episodes.get(position);
        holder.mIdView.setText(episodes.get(position).getName());
        holder.mContentView.setText(episodes.get(position).getOverview());
        Picasso.get()
                .load(episodes.get(position).getStill())
                .into(holder.mStill);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mStill;
        public TVEpisode mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mStill = view.findViewById(R.id.still);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    public void setEpisodeList(List<TVEpisode> episodeList){
        episodes.clear();
        episodes.addAll(episodeList);
        notifyDataSetChanged();
    }
}
