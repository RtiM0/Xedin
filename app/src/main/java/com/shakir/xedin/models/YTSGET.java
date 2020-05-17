package com.shakir.xedin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class YTSGET {
    @SerializedName("data")
    @Expose
    private data data;

    public YTSGET.data getData() {
        return data;
    }

    public void setData(YTSGET.data data) {
        this.data = data;
    }

    public String[] getTitle() throws NullPointerException {
        String[] a;
        try {
            int size = getData().getMovies().get(0).getTorrents().size();
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                YTSGET.data.movies.torrents torrent = getData().getMovies().get(0).getTorrents().get(i);
                String title = "YTS " + torrent.getQuality() + " " + torrent.getType() + "\nSEEDS:" + torrent.getSeeds() + " SIZE:" + torrent.getSize();
                arrayList.add(title);
            }
            a = new String[size];
            a = arrayList.toArray(a);
        } catch (Exception e){
            a = new String[]{};
        }
        return a;
    }

    public String[] getMagnets() throws NullPointerException {
        String[] a;
        try {
            int size = getData().getMovies().get(0).getTorrents().size();
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                YTSGET.data.movies.torrents torrent = getData().getMovies().get(0).getTorrents().get(i);
                String magnet = "magnet:?xt=urn:btih:" + torrent.getHash() + "&dn=" + "[YTS] Xedin+loader" +
                        "&tr=http://track.one:1234/announce" +
                        "&tr=udp://track.two:80" +
                        "&tr=udp://open.demonii.com:1337/announce" +
                        "&tr=udp://tracker.openbittorrent.com:80" +
                        "&tr=udp://tracker.coppersurfer.tk:6969" +
                        "&tr=udp://glotorrents.pw:6969/announce" +
                        "&tr=udp://tracker.opentrackr.org:1337/announce" +
                        "&tr=udp://torrent.gresille.org:80/announce" +
                        "&tr=udp://p4p.arenabg.com:1337" +
                        "&tr=udp://tracker.leechers-paradise.org:6969";
                arrayList.add(magnet);
            }
            a = new String[size];
            a = arrayList.toArray(a);
        } catch (Exception e){
            a = new String[]{};
        }
        return a;
    }


    class data {
        @SerializedName("movies")
        @Expose
        private List<movies> movies;

        public List<YTSGET.data.movies> getMovies() {
            return movies;
        }

        public void setMovies(List<YTSGET.data.movies> movies) {
            this.movies = movies;
        }

        class movies {
            @SerializedName("torrents")
            @Expose
            private List<torrents> torrents;

            public List<YTSGET.data.movies.torrents> getTorrents() {
                return torrents;
            }

            public void setTorrents(List<YTSGET.data.movies.torrents> torrents) {
                this.torrents = torrents;
            }

            class torrents {
                @SerializedName("hash")
                @Expose
                private String hash;
                @SerializedName("quality")
                @Expose
                private String quality;
                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("seeds")
                @Expose
                private int seeds;
                @SerializedName("peers")
                @Expose
                private int peers;
                @SerializedName("size")
                @Expose
                private String size;

                public String getHash() {
                    return hash;
                }

                public void setHash(String hash) {
                    this.hash = hash;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getSeeds() {
                    return seeds;
                }

                public void setSeeds(int seeds) {
                    this.seeds = seeds;
                }

                public int getPeers() {
                    return peers;
                }

                public void setPeers(int peers) {
                    this.peers = peers;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }
            }
        }
    }
}
