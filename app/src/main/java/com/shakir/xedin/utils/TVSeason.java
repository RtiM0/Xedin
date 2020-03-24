package com.shakir.xedin.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shakir.xedin.models.TVEpisode;

import java.util.List;

public class TVSeason {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("air_date")
    @Expose
    private String air_date;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("poster_path")
    @Expose
    private String poster;

    @SerializedName("episodes")
    @Expose
    private List<TVEpisode> episodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<TVEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<TVEpisode> episodes) {
        this.episodes = episodes;
    }
}
