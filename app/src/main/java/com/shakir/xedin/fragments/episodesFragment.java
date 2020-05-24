package com.shakir.xedin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.activities.InfoPage;
import com.shakir.xedin.adapters.EpisodesAdapter;
import com.shakir.xedin.interfaces.TMDBApiService;
import com.shakir.xedin.models.TVEpisode;
import com.shakir.xedin.utils.TVSeason;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class episodesFragment extends Fragment implements EpisodesAdapter.EventListener {

    private OnListFragmentInteractionListener mListener;
    private TVSeason tvSeason;
    private int season;

    public episodesFragment() {
    }

    public void setData(TVSeason tvSeason) {
        this.tvSeason = tvSeason;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        int color = getArguments().getInt("color", 0);
        String[] sNames = getArguments().getStringArray("sNames");
        int tmdbid = getArguments().getInt("tmdb");
        View view = inflater.inflate(R.layout.fragment_episodes_list, container, false);
        Spinner seasoner = view.findViewById(R.id.seasonSpinner);
        assert sNames != null;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, sNames);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        seasoner.setAdapter(arrayAdapter);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        EpisodesAdapter episodesAdapter = new EpisodesAdapter(mListener, this, context);
        recyclerView.setAdapter(episodesAdapter);
        if (color != 0) {
            recyclerView.setBackgroundColor(color);
            seasoner.setBackgroundColor(color);
        }
        seasoner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    episodesAdapter.setEpisodeList(tvSeason.getEpisodes());
                } else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://api.themoviedb.org/3/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    TMDBApiService service = retrofit.create(TMDBApiService.class);
                    service.getEpisodes(tmdbid, position + 1, BuildConfig.API_KEY).enqueue(new Callback<TVSeason>() {
                        @Override
                        public void onResponse(Call<TVSeason> call, Response<TVSeason> response) {
                            TVSeason tvSeason = response.body();
                            assert tvSeason != null;
                            episodesAdapter.setEpisodeList(tvSeason.getEpisodes());
                        }

                        @Override
                        public void onFailure(Call<TVSeason> call, Throwable t) {
                            call.clone().enqueue(this);
                            t.printStackTrace();
                        }
                    });
                }
                season = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public void setTVData(int episode, String service) {
        InfoPage infoPage = (InfoPage) getActivity();
        assert infoPage != null;
        infoPage.season.setText((season + ""));
        infoPage.episode.setText((episode + ""));
        if (service.equals("play")) {
            infoPage.performPlay();
        } else {
            infoPage.torrents.performClick();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(TVEpisode item);
    }
}
