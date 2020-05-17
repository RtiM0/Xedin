package com.shakir.xedin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TPBGET {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("info_hash")
    @Expose
    private String info_hash;
    @SerializedName("seeders")
    @Expose
    private String seeds;
    @SerializedName("size")
    @Expose
    private String size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo_hash() {
        return info_hash;
    }

    public void setInfo_hash(String info_hash) {
        this.info_hash = info_hash;
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public String getSize() {
        double s = Double.valueOf(size) / 1048576;
        String d = " MB";
        if (s >= 1024) {
            s = s / 1024;
            d = " GB";
        }
        s = Math.round(s * 10) / 10.0;
        String si = s + d;
        return si;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMagnet() {
        String mag = "magnet:?xt=urn:btih:" + getInfo_hash() + "&dn=" + getName() +
                "&tr=udp://tracker.coppersurfer.tk:6969/announce" +
                "&tr=udp://9.rarbg.to:2920/announce" +
                "&tr=udp://tracker.opentrackr.org:1337" +
                "&tr=udp://tracker.internetwarriors.net:1337/announce" +
                "&tr=udp://tracker.leechers-paradise.org:6969/announce" +
                "&tr=udp://tracker.coppersurfer.tk:6969/announce" +
                "&tr=udp://tracker.pirateparty.gr:6969/announce" +
                "&tr=udp://tracker.cyberia.is:6969/announce";
        return mag;
    }

    public String getTitle() {
        String n = getName() + "\nSEEDS:" + getSeeds() + " SIZE:" + getSize();
        return n;
    }
}
