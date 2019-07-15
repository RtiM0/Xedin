package com.shakir.xedin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.TorrentOptions;
import com.github.se_bastiaan.torrentstream.TorrentStream;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class TorrentStreamer extends AppCompatActivity implements TorrentListener {

    private static final String TORRENT = "Torrent";

    private Button button;
    private ProgressBar progressBar;
    private TorrentStream torrentStream;
    private ListView torrentsList;
    private Button button1;
    private LinearLayout infobox;
    private TextView prop;

    private String streamUrl = "";
    private String season = "";
    private String episode = "";
    private String stat;
    private Boolean plus;
    private String year = "";
    private JsonArray informer;
    private JsonArray yts = null;
    private int ready = 0;
    private ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torrent_stream);

        button = findViewById(R.id.streambutton);
        progressBar = findViewById(R.id.progressBar);
        TextView textView = findViewById(R.id.titler);
        TextView sub = findViewById(R.id.sub);
        torrentsList = findViewById(R.id.listof);
        button1 = findViewById(R.id.plbutton);
        infobox = findViewById(R.id.infobox);
        prop = findViewById(R.id.prop);

        String action = getIntent().getAction();
        String title = getIntent().getStringExtra("title");
        String imdb = getIntent().getStringExtra("imdb");
        int tmdbid = getIntent().getIntExtra("tmdbid", -1);
        plus = getIntent().getBooleanExtra("plusmo", true);
        textView.setText(title);
        stat = getIntent().getStringExtra("status");

//        Ion.with(this)
//                .load("https://api.themoviedb.org/3/movie/"+tmdbid+"?api_key="+BuildConfig.API_KEY+"&language=en-US")
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        year = result.get("release_date").getAsString().substring(0,4);
//                    }
//                });

        if (stat.equals("TV")) {
            sub.setVisibility(View.VISIBLE);
            season = getIntent().getStringExtra("season");
            episode = getIntent().getStringExtra("episode");
            sub.setText("Season " + season + " Episode " + episode);
            sub.setVisibility(View.VISIBLE);
        }

        String pirateapi = "http://tpbc.herokuapp.com/search/";
        String add = title.replaceAll(" ", "_");
        add = add.replaceAll("-", "_");
        add = add.replaceAll(":", "");
        add = add.replaceAll("'", "");
        if (stat.equals("TV")) {
            int s = Integer.parseInt(season);
            int e = Integer.parseInt(episode);
            if (!plus) {
                if (s < 10) {
                    add = add + "_s0" + s;
                } else {
                    add = add + "_s" + s;
                }
                if (e < 10) {
                    add = add + "e0" + e;
                } else {
                    add = add + "e" + e;
                }
            } else {
                add = add + "_season_" + s;
            }
        } else {
            String q = "https://yts.lt/api/v2/list_movies.json?query_term=" + imdb + "&limit=1";
            Ion.with(this)
                    .load(q)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
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
                            }
                        }
                    });
        }
        Log.d("Query Name:", pirateapi + add);
        Ion.with(this)
                .load(pirateapi + add)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jo;
                        informer = result;
                        for (int i = 0; i < result.size(); i++) {
                            jo = (JsonObject) result.get(i);
                            names.add(jo.get("title").getAsString() + "\n[SEEDS:" + jo.get("seeds").getAsInt() + "]");
                        }
                        listviewbuilder();
                    }
                });

        Uri data = getIntent().getData();
        if (action != null && action.equals(Intent.ACTION_VIEW) && data != null) {
            try {
                streamUrl = URLDecoder.decode(data.toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "NOPE", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                if (torrentStream.isStreaming()) {
                    torrentStream.stopStream();
                    button.setText("Start stream");
                    return;
                }
                torrentStream.startStream(streamUrl);
                button.setText("Stop stream");
            }
        });
        if (stat.equals("TV") && plus) {
            TorrentOptions torrentOptions = new TorrentOptions.Builder()
                    .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                    .removeFilesAfterStop(true)
                    .autoDownload(false)
                    .build();

            torrentStream = TorrentStream.init(torrentOptions);
            torrentStream.addListener(this);
        } else {
            TorrentOptions torrentOptions = new TorrentOptions.Builder()
                    .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                    .removeFilesAfterStop(true)
                    .build();

            torrentStream = TorrentStream.init(torrentOptions);
            torrentStream.addListener(this);
        }

        progressBar.setMax(100);

    }

    private void listviewbuilder() {
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.torrent_list_items, R.id.textV, names);
        torrentsList.setAdapter(mArrayAdapter);
        torrentsList.setVisibility(View.VISIBLE);
        torrentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stat.equals("TV")) {
                    JsonObject nib = (JsonObject) informer.get(position);
                    streamUrl = nib.get("magnet").getAsString();
                } else {
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
                        }
                        if (position >= yts.size()) {
                            JsonObject nib = (JsonObject) informer.get(position - yts.size());
                            streamUrl = nib.get("magnet").getAsString();
                        }
                    } else {
                        JsonObject nib = (JsonObject) informer.get(position);
                        streamUrl = nib.get("magnet").getAsString();
                    }
                }

                Log.d("Magnet", streamUrl);
                torrentsList.setVisibility(View.GONE);
                infobox.setVisibility(View.VISIBLE);
                ready = 1;
                torrentStream.startStream(streamUrl);
                if (!plus && stat.equals("Movie")) {
                    button.setText("Stop stream");
                }
            }
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
            torrentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    torrent.setSelectedFileIndex(position);
                    torrent.startDownload();
                    torrentsList.setVisibility(View.GONE);
                    infobox.setVisibility(View.VISIBLE);
                }
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
    }

    @Override
    public void onStreamReady(final Torrent torrent) {
        progressBar.setProgress(100);
        Log.d(TORRENT, "onStreamReady: " + torrent.getVideoFile());
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openstream(torrent);
            }
        });
        openstream(torrent);
    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {
        if (status.bufferProgress <= 100 && progressBar.getProgress() < 100 && progressBar.getProgress() != status.bufferProgress) {
            Log.d(TORRENT, "Progress: " + status.bufferProgress);
            progressBar.setProgress(status.bufferProgress);
        }
        prop.setText("File Name: " + torrent.getVideoFile().getName() + "\nConnected: " + status.seeds);
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
