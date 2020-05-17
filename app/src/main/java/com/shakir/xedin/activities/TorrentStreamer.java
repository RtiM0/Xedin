package com.shakir.xedin.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
    private String streamUrl = "";

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
        torrentsList = findViewById(R.id.listof);
        button1 = findViewById(R.id.plbutton);
        infobox = findViewById(R.id.infobox);
        prop = findViewById(R.id.prop);

        String action = getIntent().getAction();
        String title = getIntent().getStringExtra("title");
        streamUrl = getIntent().getStringExtra("StreamUrl");
        if(title!=null) {
            textView.setText(title);
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
        TorrentOptions torrentOptions = new TorrentOptions.Builder()
                .saveLocation(getExternalCacheDir())
                .removeFilesAfterStop(true)
                .autoDownload(false)
                .build();

        torrentStream = TorrentStream.init(torrentOptions);
        torrentStream.addListener(this);
        button.performClick();
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
            torrentsList.setVisibility(View.VISIBLE);
            infobox.setVisibility(View.GONE);
            ArrayAdapter<String> nArrayAdapter = new ArrayAdapter<>(this, R.layout.torrent_list_items, R.id.textV, torrent.getFileNames());
            torrentsList.setAdapter(nArrayAdapter);
            torrentsList.setOnItemClickListener((parent, view, position, id) -> {
                torrent.setSelectedFileIndex(position);
                ((TextView)findViewById(R.id.sub)).setText(torrent.getFileNames()[position]);
                torrent.startDownload();
                torrentsList.setVisibility(View.GONE);
                infobox.setVisibility(View.VISIBLE);
            });
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
        super.onBackPressed();
    }
}