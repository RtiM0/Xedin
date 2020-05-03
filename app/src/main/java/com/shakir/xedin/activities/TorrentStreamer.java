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
import com.shakir.xedin.R;
import com.shakir.xedin.interfaces.TMDBApiService;
import com.shakir.xedin.models.TPBGET;
import com.shakir.xedin.models.YTSGET;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("SetTextI18n")
public class TorrentStreamer extends AppCompatActivity implements TorrentListener {

    private static final String TORRENT = "Torrent";
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
            searchTPB("?q=" + title);
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
                searchTPB("?q=" + add);
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
                searchTPB("?q=" + add);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://yts.mx/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TMDBApiService apiService = retrofit.create(TMDBApiService.class);
        apiService.getYTS("https://yts.mx/api/v2/list_movies.json?query_term=" + imdb + "&limit=1")
                .enqueue(new Callback<YTSGET>() {
                    @Override
                    public void onResponse(Call<YTSGET> call, Response<YTSGET> response) {
                        YTSGET ytsget = response.body();
                        Collections.addAll(names, ytsget.getTitle());
                        Collections.addAll(mags, ytsget.getMagnets());
                        listviewbuilder();
                    }

                    @Override
                    public void onFailure(Call<YTSGET> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "YTS not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchTPB(String addon) {
        String[] apis = {"https://api.thepiratebay.workers.dev", "https://api.tpb.workers.dev", "https://api.apibay.workers.dev"};
        String server = apis[new Random().nextInt(apis.length)];
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apibay.gq/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TMDBApiService apiService = retrofit.create(TMDBApiService.class);
        apiService.getTPB(server + "/q.php" + addon)
                .enqueue(new Callback<List<TPBGET>>() {
                    @Override
                    public void onResponse(Call<List<TPBGET>> call, Response<List<TPBGET>> response) {
                        for (TPBGET torrent : response.body()) {
                            names.add(torrent.getTitle());
                            mags.add(torrent.getMagnet());
                        }
                        listviewbuilder();
                    }

                    @Override
                    public void onFailure(Call<List<TPBGET>> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to load TPB", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void listviewbuilder() {
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.torrent_list_items, R.id.textV, names);
        torrentsList.setAdapter(mArrayAdapter);
        torrentsList.setVisibility(View.VISIBLE);
        torrentsList.setOnItemClickListener((parent, view, position, id) -> {
            streamUrl = mags.get(position);
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