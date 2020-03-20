package com.shakir.xedin.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.TorrentOptions;
import com.github.se_bastiaan.torrentstream.TorrentStream;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.shakir.xedin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("SetTextI18n")
public class TorrentStreamer extends AppCompatActivity implements TorrentListener {

    private static final String TORRENT = "Torrent";
    private static final String TPB_URL = "https://piratebayztemzmv.onion.ly";
    private Button button;
    private TorrentStream torrentStream;
    private ListView torrentsList;
    private Button button1;
    private LinearLayout infobox;
    private TextView prop;
    private RoundCornerProgressBar progressBar;
    private Button mag;
    private String streamUrl = "";
    private String stat;
    private Boolean plus;
    private JsonArray yts = null;
    private int ready = 0;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> mags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torrent_stream);

        button = findViewById(R.id.streambutton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressColor(Color.parseColor("#008080"));
        progressBar.setProgressBackgroundColor(Color.parseColor("#808080"));
        progressBar.setMax(100);
        TextView textView = findViewById(R.id.titler);
        TextView sub = findViewById(R.id.sub);
        torrentsList = findViewById(R.id.listof);
        button1 = findViewById(R.id.plbutton);
        infobox = findViewById(R.id.infobox);
        prop = findViewById(R.id.prop);
        mag = findViewById(R.id.magneter);

        String action = getIntent().getAction();
        String title = getIntent().getStringExtra("title");
        String imdb = getIntent().getStringExtra("imdb");
        plus = getIntent().getBooleanExtra("plusmo", true);
        textView.setText(title);
        stat = getIntent().getStringExtra("status");

        if (!stat.equals("TV")) {
            searchYTS(imdb);
            searchTPB(TPB_URL + "/s/?q=" + title);
        } else {
            sub.setVisibility(View.VISIBLE);
            String season = getIntent().getStringExtra("season");
            String episode = getIntent().getStringExtra("episode");
            sub.setText("Season " + season + " Episode " + episode);
            sub.setVisibility(View.VISIBLE);
            String add = title;
            int s = Integer.parseInt(season);
            int e = Integer.parseInt(episode);
            if (plus) {
                add = add + " season " + s;
                searchTPB(TPB_URL + "/s/?q=" + add);
            } else {
                if (s < 10) {
                    add = add + " s0" + s;
                } else {
                    add = add + " s" + s;
                }
                if (e < 10) {
                    add = add + "e0" + e;
                } else {
                    add = add + "e" + e;
                }
                searchTPB(TPB_URL + "/s/?q=" + add);
            }
        }

        Uri data = getIntent().getData();
        if (action != null && action.equals(Intent.ACTION_VIEW) && data != null) {
            try {
                streamUrl = URLDecoder.decode(data.toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "NOPE", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        button.setOnClickListener(v -> {
            progressBar.setProgress(0);
            if (torrentStream.isStreaming()) {
                torrentStream.stopStream();
                button.setText("Start stream");
                return;
            }
            torrentStream.startStream(streamUrl);
            button.setText("Stop stream");
        });

        if (stat.equals("TV") && plus) {
            TorrentOptions torrentOptions = new TorrentOptions.Builder()
                    .saveLocation(Objects.requireNonNull(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)))
                    .removeFilesAfterStop(true)
                    .autoDownload(false)
                    .build();

            torrentStream = TorrentStream.init(torrentOptions);
            torrentStream.addListener(this);
        } else {
            TorrentOptions torrentOptions = new TorrentOptions.Builder()
                    .saveLocation(Objects.requireNonNull(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)))
                    .removeFilesAfterStop(true)
                    .build();

            torrentStream = TorrentStream.init(torrentOptions);
            torrentStream.addListener(this);
        }

    }

    private void searchYTS(String imdb) {
        String q = "https://yts.mx/api/v2/list_movies.json?query_term=" + imdb + "&limit=1";
        Ion.with(this)
                .load(q)
                .asJsonObject()
                .setCallback((e, result) -> {
                    try {
                        JsonObject r = result.getAsJsonObject("data");
                        JsonArray p = r.getAsJsonArray("movies");
                        if (p != null) {
                            JsonObject a = (JsonObject) p.get(0);
                            yts = a.getAsJsonArray("torrents");
                            JsonObject tor;
                            for (int i = 0; i < yts.size(); i++) {
                                tor = (JsonObject) yts.get(i);
                                names.add("YTS " + tor.get("quality").getAsString() + " " + tor.get("type").getAsString() + "\n[SEEDS:" + tor.get("seeds").getAsInt() + " SIZE:" + tor.get("size").getAsString() + "]");
                            }
                            listviewbuilder();
                        }
                    } catch (NullPointerException e1) {
                        Toast.makeText(getApplicationContext(), "YTS not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchTPB(String url) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(url).get();
                Element data = doc.getElementById("searchResult");
                Elements titles = data.getElementsByClass("detName");
                Elements magnets = data.removeClass("detName").select("a[href]");
                int i = 0;
                for (Element title : titles) {
                    names.add(title.text());
                }
                for (Element magnet : magnets) {
                    if (magnet.attr("href").contains("magnet") && i < 30) {
                        mags.add(magnet.attr("href"));
                        i++;
                    } else if (i >= 30) {
                        break;
                    }
                }
            } catch (IOException | NullPointerException e) {
                Log.d("run: ", Objects.requireNonNull(e.getMessage()));
            }
            runOnUiThread(this::listviewbuilder);
        }).start();
    }

    private void listviewbuilder() {
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.torrent_list_items, R.id.textV, names);
        torrentsList.setAdapter(mArrayAdapter);
        torrentsList.setVisibility(View.VISIBLE);
        torrentsList.setOnItemClickListener((parent, view, position, id) -> {
            if (yts != null) {
                if (position < yts.size()) {
                    JsonObject yib = (JsonObject) yts.get(position);
                    streamUrl = "magnet:?xt=urn:btih:" + yib.get("hash").getAsString() + "&dn=" + "Xedin+loader" +
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
                } else {
                    streamUrl = mags.get(position - yts.size());
                }
            } else {
                streamUrl = mags.get(position);
            }
            Log.d("Magnet", streamUrl);
            torrentsList.setVisibility(View.GONE);
            infobox.setVisibility(View.VISIBLE);
            ready = 1;
            Log.d("listviewbuilder: ", streamUrl);
            torrentStream.startStream(streamUrl);
            mag.setVisibility(View.VISIBLE);
            mag.setOnClickListener(v -> {
                Intent magn = new Intent(Intent.ACTION_VIEW, Uri.parse(streamUrl));
                startActivity(magn);
            });
            button.setText("Stop Stream");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onStreamPrepared(Torrent torrent) {
        Log.d(TORRENT, "OnStreamPrepared");
        // If you set TorrentOptions#autoDownload(false) then this is probably the place to call
        // torrent.startDownload();
        if (plus && stat.equals("TV")) {
            torrentsList.setVisibility(View.VISIBLE);
            infobox.setVisibility(View.GONE);
            ArrayAdapter<String> nArrayAdapter = new ArrayAdapter<>(this, R.layout.torrent_list_items, R.id.textV, torrent.getFileNames());
            torrentsList.setAdapter(nArrayAdapter);
            torrentsList.setOnItemClickListener((parent, view, position, id) -> {
                torrent.setSelectedFileIndex(position);
                torrent.startDownload();
                torrentsList.setVisibility(View.GONE);
                infobox.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void onStreamStarted(Torrent torrent) {
        Log.d(TORRENT, "OnStreamStarted");

    }

    @Override
    public void onStreamError(Torrent torrent, Exception e) {
        button.setText("Start stream");
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Try Again Plox", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStreamReady(final Torrent torrent) {
        progressBar.setProgress(100);
        Log.d(TORRENT, "onStreamReady: " + torrent.getVideoFile());
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> openstream(torrent));
        openstream(torrent);
    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {
        if (status.bufferProgress <= 100 && progressBar.getProgress() < 100 && progressBar.getProgress() != status.bufferProgress) {
            Log.d(TORRENT, "Progress: " + status.bufferProgress);
            progressBar.setProgress(status.bufferProgress);
        }
        prop.setText("File Name: " + torrent.getVideoFile().getName() + "\nConnected: " + status.seeds + "\nTotal Progress: " + Math.round(status.progress * 100.0) / 100.0 + "%");
    }

    @Override
    public void onStreamStopped() {
        Log.d(TORRENT, "onStreamStopped ");
    }

    void openstream(Torrent torrent) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(torrent.getVideoFile().toString()));
        intent.setDataAndType(Uri.parse(torrent.getVideoFile().toString()), "video/mp4");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        torrentStream.stopStream();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (ready == 1) {
            torrentsList.setVisibility(View.VISIBLE);
            infobox.setVisibility(View.GONE);
            ready = 0;
        } else {
            super.onBackPressed();
        }
    }
}