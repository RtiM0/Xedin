package com.shakir.xedin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.utils.FullScreenClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class InfoPage extends AppCompatActivity {

    private RelativeLayout backs;
    private TextView tet;
    private ImageView backd;
    private TextView disc;
    private Button play;
    private EditText season;
    private EditText episode;
    private String status;
    private WebView webView;
    private int tmdbid;
    private String imdb;
    private Switch aSwitch;
    private Button torrents;
    private int immersive = 0;
    private Switch bSwitch;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String name = intent.getStringExtra("tvname");
        String back = intent.getStringExtra("backdrop");
        String desc = intent.getStringExtra("desc");
        tmdbid = intent.getIntExtra("tmdbid", -1);
        String poster = intent.getStringExtra("poster");
        setContentView(R.layout.activity_info_page);
        tet = findViewById(R.id.titel);
        backd = findViewById(R.id.backd);
        ImageView post = findViewById(R.id.postar);
        disc = findViewById(R.id.description);
        backs = findViewById(R.id.backs);
        play = findViewById(R.id.playbutton);
        season = findViewById(R.id.season);
        episode = findViewById(R.id.episode);
        webView = findViewById(R.id.player);
        aSwitch = findViewById(R.id.switch1);
        torrents = findViewById(R.id.torrent);
        bSwitch = findViewById(R.id.switch2);
        ConstraintLayout parental = findViewById(R.id.parental);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (name == null) {
            tet.setText(title);
            status = "Movie";
        } else {
            tet.setText(name);
            status = "TV";
            season.setVisibility(View.VISIBLE);
            episode.setVisibility(View.VISIBLE);
            bSwitch.setVisibility(View.VISIBLE);
        }
        disc.setText(desc);
        Picasso.get()
                .load(poster)
                .placeholder(R.color.colorAccent)
                .into(post);
        Picasso.get()
                .load(back)
                .placeholder(R.color.colorAccent)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert backd != null;
                        backd.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@Nullable Palette palette) {
                                        try {
                                            Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                            backs.setBackgroundColor(textSwatch.getRgb());
                                            tet.setTextColor(textSwatch.getBodyTextColor());
                                            disc.setTextColor(textSwatch.getTitleTextColor());
                                            aSwitch.setTextColor(textSwatch.getBodyTextColor());
                                            aSwitch.setHighlightColor(textSwatch.getRgb());
                                            bSwitch.setTextColor(textSwatch.getBodyTextColor());
                                            bSwitch.setHighlightColor(textSwatch.getRgb());
                                            getWindow().setStatusBarColor(textSwatch.getRgb());
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebChromeClient(new FullScreenClient(parental, backs));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (status == "Movie") {
            imdbSources("https://api.themoviedb.org/3/movie/" + tmdbid + "/external_ids?api_key=" + BuildConfig.API_KEY);
        } else {
            imdbSources("http://api.themoviedb.org/3/tv/" + tmdbid + "/external_ids?api_key=" + BuildConfig.API_KEY);
        }
    }

    void imdbSources(String url) {
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (status.equals("Movie")) {
                            imdb = result.get("imdb_id").getAsString();
                        } else {
                            imdb = result.get("imdb_id").getAsString();
                        }
                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playVidSrc(imdb);
                            }
                        });
                        play.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(InfoPage.this, play);
                                popupMenu.getMenuInflater()
                                        .inflate(R.menu.play_menu, popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().toString().equals("VideoSpider")) {
                                            playVideoSpider();
                                            return true;
                                        } else if (item.getTitle().toString().equals("ODB")) {
                                            playODB(imdb);
                                            return true;
                                        } else if (item.getTitle().toString().equals("Vplus")) {
                                            playVplus(imdb);
                                            return true;
                                        } else {
                                            playVidSrc(imdb);
                                            return true;
                                        }
                                    }
                                });
                                popupMenu.show();
                                return true;
                            }
                        });
                        torrents.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(InfoPage.this, TorrentStreamer.class);
                                //intent.putExtra("magnet", magnet);
                                intent.putExtra("title", tet.getText().toString());
                                intent.putExtra("status", status);
                                intent.putExtra("imdb", imdb);
                                intent.putExtra("tmdbid", tmdbid);
                                intent.putExtra("plusmo", bSwitch.isChecked());
                                if (status.equals("TV")) {
                                    intent.putExtra("season", season.getText().toString());
                                    intent.putExtra("episode", episode.getText().toString());
                                }
                                InfoPage.this.startActivity(intent);
                            }
                        });
                    }
                });
    }

    void playVideoSpider() {
        String loader;
        if (status.equals("TV")) {
            loader = "https://videospider.stream/personal?key=" + BuildConfig.SPIDER_KEY + "&video_id=" + tmdbid + "&tmdb=1&tv=1&s=" + season.getText().toString() + "&e=" + episode.getText().toString();
        } else {
            loader = "https://videospider.stream/personal?key=" + BuildConfig.SPIDER_KEY + "&video_id=" + tmdbid + "&tmdb=1";
        }
        if (aSwitch.isChecked()) {
            iframe(loader);
        } else {
            browsa(loader);
        }
    }

    void playODB(String finalImdb) {
        String url;
        if (status.equals("TV")) {
            url = "https://api.odb.to/embed?imdb_id=" + finalImdb + "&s=" + season.getText().toString() + "&e=" + episode.getText().toString() + "&cc=eng";
        } else {
            url = "https://api.odb.to/embed?imdb_id=" + finalImdb + "&cc=eng";
        }
        if (aSwitch.isChecked()) {
            iframe(url);
        } else {
            browsa(url);
        }
    }

    void playVplus(String finalImdb) {
        String url = "http://vplus.ucoz.com/" + finalImdb;
        if (aSwitch.isChecked()) {
            iframe(url);
        } else {
            browsa(url);
        }
    }

    void playVidSrc(String finalImdb) {
        String uri;
        if (status.equals("TV")) {
            uri = "https://vidsrc.me/embed/" + finalImdb + "/" + season.getText().toString() + "-" + episode.getText().toString() + "/";
        } else {
            uri = "https://vidsrc.me/embed/" + finalImdb + "/";
        }
        if (aSwitch.isChecked()) {
            iframe(uri);
        } else {
            browsa(uri);
        }
    }

    private void iframe(String url) {
        int width = backs.getMeasuredWidth();
        int height = (width * 9) / 16;
        String s = "<iframe src='" + url + "'/ width='" + width + "' height='" + height + "' frameBorder='0' allowfullscreen='true' scrolling='no'></iframe>";
        webView.setVisibility(View.VISIBLE);
        hideSystemUI();
        webView.loadData(s, "text/html", "UTF-8");
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private void browsa(String url) {
        Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(openBrowser);
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        immersive = 1;
        getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        immersive = 0;
        getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    @Override
    public void onBackPressed() {
        if (immersive == 1) {
            showSystemUI();
            immersive = 0;
            webView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}