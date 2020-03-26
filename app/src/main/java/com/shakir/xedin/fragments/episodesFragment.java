package com.shakir.xedin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.R;
import com.shakir.xedin.activities.InfoPage;
import com.shakir.xedin.adapters.EpisodesAdapter;
import com.shakir.xedin.models.TVEpisode;
import com.shakir.xedin.utils.TVSeason;

import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class episodesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private TVSeason tvSeason;

    public episodesFragment() {
    }

    public void setData(TVSeason tvSeason) {
        this.tvSeason = tvSeason;
    }

    public int getEpisode() {

        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int color = getArguments().getInt("color", 0);
        View view = inflater.inflate(R.layout.fragment_episodes_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            EpisodesAdapter episodesAdapter = new EpisodesAdapter(mListener);
            recyclerView.setAdapter(episodesAdapter);
            if (color != 0) {
                recyclerView.setBackgroundColor(color);
            }
            episodesAdapter.setEpisodeList(tvSeason.getEpisodes());
            ((InfoPage) Objects.requireNonNull(getActivity())).setFragmentRefreshListener(() -> {
                episodesAdapter.setEpisodeList(tvSeason.getEpisodes());
            });
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TVEpisode item);
    }
}
