package com.shakir.xedin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shakir.xedin.utils.TVSeason;

import java.util.ArrayList;
import java.util.List;

public class MediaDetail {
    @SerializedName("number_of_seasons")
    @Expose
    private int nSeasons;

    @SerializedName("external_ids")
    @Expose
    private ExternalIds externalIds;

    @SerializedName("season/1")
    @Expose
    private TVSeason tvSeason;

    @SerializedName("seasons")
    @Expose
    private List<Seasons> seasons;

    public int getnSeasons() {
        return nSeasons;
    }

    public void setnSeasons(int nSeasons) {
        this.nSeasons = nSeasons;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    public TVSeason getTvSeason() {
        return tvSeason;
    }

    public void setTvSeason(TVSeason tvSeason) {
        this.tvSeason = tvSeason;
    }

    public String getImdb() {
        return getExternalIds().getImdb_id();
    }

    public String[] sNames() {
        int size = getnSeasons();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (getSeasons().get(i).getsNum() != 0) {
                arrayList.add(getSeasons().get(i).getsName());
            }
        }
        String[] a = new String[arrayList.size()];
        a = arrayList.toArray(a);
        return a;
    }

    public List<Seasons> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Seasons> seasons) {
        this.seasons = seasons;
    }

    class ExternalIds {
        @SerializedName("imdb_id")
        @Expose
        private String imdb_id;

        String getImdb_id() {
            return imdb_id;
        }

        public void setImdb_id(String imdb_id) {
            this.imdb_id = imdb_id;
        }
    }

    class Seasons {
        @SerializedName("name")
        @Expose
        private String sName;

        @SerializedName("season_number")
        @Expose
        private int sNum;

        public String getsName() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName = sName;
        }

        public int getsNum() {
            return sNum;
        }

        public void setsNum(int sNum) {
            this.sNum = sNum;
        }
    }
}
