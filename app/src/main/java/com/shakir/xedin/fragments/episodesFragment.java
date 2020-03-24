package com.shakir.xedin.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.adapters.EpisodesAdapter;
import com.shakir.xedin.interfaces.TMDBApiService;
import com.shakir.xedin.models.TVEpisode;
import com.shakir.xedin.utils.TVSeason;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class episodesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    public episodesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int tmdb = getArguments() != null ? getArguments().getInt("tmdb") : 0;
        View view = inflater.inflate(R.layout.fragment_episodes_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            EpisodesAdapter episodesAdapter = new EpisodesAdapter(mListener);
            recyclerView.setAdapter(episodesAdapter);
            recyclerView.setNestedScrollingEnabled(false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TMDBApiService service = retrofit.create(TMDBApiService.class);
            service.getEpisodes(tmdb,1, BuildConfig.API_KEY).enqueue(new Callback<TVSeason>() {
                @Override
                public void onResponse(Call<TVSeason> call, Response<TVSeason> response) {
                    TVSeason tvSeason = response.body();
                    episodesAdapter.setEpisodeList(tvSeason.getEpisodes());
                }

                @Override
                public void onFailure(Call<TVSeason> call, Throwable t) {

                }
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
