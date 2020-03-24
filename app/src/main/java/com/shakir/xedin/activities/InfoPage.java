package com.shakir.xedin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.koushikdutta.ion.Ion;
import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.fragments.episodesFragment;
import com.shakir.xedin.utils.FullScreenClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class InfoPage extends AppCompatActivity {

    private ConstraintLayout parental;
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
        RelativeLayout backs = findViewById(R.id.backs);
        play = findViewById(R.id.playbutton);
        season = findViewById(R.id.season);
        episode = findViewById(R.id.episode);
        webView = findViewById(R.id.player);
        aSwitch = findViewById(R.id.switch1);
        torrents = findViewById(R.id.torrent);
        bSwitch = findViewById(R.id.switch2);
        parental = findViewById(R.id.parental);
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
                                .generate(palette -> {
                                    try {
                                        assert palette != null;
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                        assert textSwatch != null;
                                        parental.setBackgroundColor(textSwatch.getRgb());
                                        tet.setTextColor(textSwatch.getBodyTextColor());
                                        disc.setTextColor(textSwatch.getTitleTextColor());
                                        aSwitch.setTextColor(textSwatch.getBodyTextColor());
                                        aSwitch.setHighlightColor(textSwatch.getRgb());
                                        bSwitch.setTextColor(textSwatch.getBodyTextColor());
                                        bSwitch.setHighlightColor(textSwatch.getRgb());
                                        getWindow().setStatusBarColor(textSwatch.getRgb());
                                        getWindow().setNavigationBarColor(textSwatch.getRgb());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
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
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new FullScreenClient(parental, backs));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (status.equals("Movie")) {
            imdbSources("https://api.themoviedb.org/3/movie/" + tmdbid + "/external_ids?api_key=" + BuildConfig.API_KEY);
        } else {
            imdbSources("http://api.themoviedb.org/3/tv/" + tmdbid + "/external_ids?api_key=" + BuildConfig.API_KEY);
            Bundle bundle = new Bundle();
            bundle.putInt("tmdb",tmdbid);
            Fragment details = new episodesFragment();
            details.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.tvdetails,details,"1").commit();
        }
    }

    void imdbSources(String url) {
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if (status.equals("Movie")) {
                        imdb = result.get("imdb_id").getAsString();
                    } else {
                        imdb = result.get("imdb_id").getAsString();
                    }
                    play.setOnClickListener(v -> playVidSrc(imdb));
                    play.setOnLongClickListener(v -> {
                        PopupMenu popupMenu = new PopupMenu(InfoPage.this, play);
                        popupMenu.getMenuInflater()
                                .inflate(R.menu.play_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(item -> {
                            String playitem = item.getTitle().toString();
                            switch (playitem) {
                                case "VideoSpider":
                                    playVideoSpider();
                                    return true;
                                case "GDrivePlayer":
                                    playGDP(imdb);
                                    return true;
                                case "123Files":
                                    play123();
                                    return true;
                                case "Free Streaming":
                                    playFS();
                                    break;
                                default:
                                    playVidSrc(imdb);
                                    return true;
                            }
                            return false;
                        });
                        popupMenu.show();
                        return true;
                    });
                    torrents.setOnClickListener(v -> {
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
                    });
                });
    }

    private void playGDP(String imdb) {
        String uri;
        if (status.equals("TV")) {
            uri = "https://database.gdriveplayer.us/player.php?type=series&imdb=" + imdb + "&season=" + season.getText().toString() + "&episode=" + episode.getText().toString();
        } else {
            uri = "http://database.gdriveplayer.us/player.php?imdb=" + imdb;
        }
        if (aSwitch.isChecked()) {
            iframe(uri);
        } else {
            browsa(uri);
        }
    }

    private void play123() {
        String uri;
        if (status.equals("TV")) {
            uri = "https://123files.club/imdb/tmdb/tv/?id=" + tmdbid + "&s=" + season.getText().toString() + "&e=" + episode.getText().toString();
        } else {
            uri = "https://123files.club/imdb/tmdb/movie/?id=" + tmdbid;
        }
        if (aSwitch.isChecked()) {
            iframe(uri);
        } else {
            browsa(uri);
        }
    }

    private void playFS() {
        String uri;
        if (status.equals("TV")) {
            uri = "https://fsapi.xyz/tv-tmdb/" + tmdbid + "-" + season.getText().toString() + "-" + episode.getText().toString();
        } else {
            uri = "https://fsapi.xyz/tmdb-movie/" + tmdbid;
        }
        if (aSwitch.isChecked()) {
            iframe(uri);
        } else {
            browsa(uri);
        }
    }

    private void playVideoSpider() {
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

    private void playVidSrc(String finalImdb) {
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = (width * 9) / 16;
        webView.getLayoutParams().width = width;
        webView.getLayoutParams().height = height;
        String s = "<iframe src='" + url + "'/ width='" + "100%" + "' height='" + "100%" + "' frameBorder='0' allowfullscreen='true' scrolling='no' style=\"position:fixed; top:0; left:0; bottom:0; right:0; width:100%; height:100%; border:none; margin:0; padding:0; overflow:hidden; z-index:999999;\"></iframe>";
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
        parental.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.KEEP_SCREEN_ON);
        immersive = 1;
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        immersive = 0;
        parental.setSystemUiVisibility(View.VISIBLE);
        webView.loadUrl("about:blank");

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

    @Override
    protected void onResume() {
        if (immersive == 1) {
            hideSystemUI();
        }
        super.onResume();
    }
}