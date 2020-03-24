package com.shakir.xedin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVEpisode {
    private static final String STILL_PATH = "https://image.tmdb.org/t/p/original";

    @SerializedName("episode_number")
    @Expose
    private int episode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("still_path")
    @Expose
    private String still;

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getStill() {
        return STILL_PATH + still;
    }

    public void setStill(String still) {
        this.still = still;
    }
}
