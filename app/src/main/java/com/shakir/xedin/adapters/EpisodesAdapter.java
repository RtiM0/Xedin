package com.shakir.xedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.shakir.xedin.R;
import com.shakir.xedin.fragments.episodesFragment;
import com.shakir.xedin.models.TVEpisode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private final List<TVEpisode> episodes;
    private final episodesFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private EventListener listener;

    public EpisodesAdapter(episodesFragment.OnListFragmentInteractionListener mlistener, EventListener listener, Context context) {
        episodes = new ArrayList<>();
        mListener = mlistener;
        mContext = context;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = episodes.get(position);
        holder.mIdView.setText(episodes.get(position).getName());
        Picasso.get()
                .load(episodes.get(position).getStill())
                .placeholder(R.drawable.still_placeholder)
                .into(holder.mStill);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
            BottomDialog dialog = new BottomDialog.Builder(mContext)
                    .setTitle(holder.mItem.getName())
                    .setContent(holder.mItem.getOverview())
                    .setPositiveText("Play")
                    .setNegativeText("Torrent")
                    .onPositive(bottomDialog -> {
                        listener.setTVData((position + 1), "play");
                    })
                    .onNegative(bottomDialog -> {
                        listener.setTVData((position + 1), "torrent");
                    })
                    .build();
            dialog.show();
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_episodes, parent, false);
        return new ViewHolder(view);
    }

    public interface EventListener {
        void setTVData(int episode, String service);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        //public final TextView mContentView;
        public final ImageView mStill;
        public TVEpisode mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mStill = view.findViewById(R.id.still);
        }
    }
    public void setEpisodeList(List<TVEpisode> episodeList){
        episodes.clear();
        episodes.addAll(episodeList);
        notifyDataSetChanged();
    }
}
