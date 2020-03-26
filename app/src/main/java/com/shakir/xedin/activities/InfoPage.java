package com.shakir.xedin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.shakir.xedin.BuildConfig;
import com.shakir.xedin.R;
import com.shakir.xedin.fragments.episodesFragment;
import com.shakir.xedin.interfaces.TMDBApiService;
import com.shakir.xedin.models.MediaDetail;
import com.shakir.xedin.utils.FullScreenClient;
import com.shakir.xedin.utils.TVSeason;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoPage extends AppCompatActivity {

    private ConstraintLayout parental;
    private TextView tet;
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
    private Spinner seasoner;
    private int flag = 0;
    private int detailColor = 0;
    private FragmentRefreshListener fragmentRefreshListener;

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    @SuppressLint({"SetJavaScriptEnabled", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String name = intent.getStringExtra("tvname");
        String desc = intent.getStringExtra("desc");
        tmdbid = intent.getIntExtra("tmdbid", -1);
        String poster = intent.getStringExtra("poster");
        setContentView(R.layout.activity_info_page);
        tet = findViewById(R.id.titel);
        disc = findViewById(R.id.description);
        LinearLayout backs = findViewById(R.id.backs);
        play = findViewById(R.id.playbutton);
        season = findViewById(R.id.season);
        episode = findViewById(R.id.episode);
        webView = findViewById(R.id.player);
        aSwitch = findViewById(R.id.switch1);
        torrents = findViewById(R.id.torrent);
        bSwitch = findViewById(R.id.switch2);
        parental = findViewById(R.id.parental);
        seasoner = findViewById(R.id.seasonSpinner);
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
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        findViewById(R.id.posta).setBackground(new BitmapDrawable(getResources(), bitmap));
                        Palette.from(bitmap)
                                .generate(palette -> {
                                    try {
                                        Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                                        Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                                        parental.setBackgroundColor(darkVibrantSwatch.getRgb());
                                        tet.setTextColor(darkVibrantSwatch.getTitleTextColor());
                                        disc.setTextColor(darkVibrantSwatch.getBodyTextColor());
                                        season.setHintTextColor(darkVibrantSwatch.getBodyTextColor());
                                        episode.setHintTextColor(darkVibrantSwatch.getBodyTextColor());
                                        aSwitch.setTextColor(darkVibrantSwatch.getBodyTextColor());
                                        aSwitch.setHighlightColor(darkVibrantSwatch.getRgb());
                                        bSwitch.setTextColor(darkVibrantSwatch.getBodyTextColor());
                                        bSwitch.setHighlightColor(darkVibrantSwatch.getRgb());
                                        seasoner.setBackgroundColor(darkMutedSwatch.getRgb());
                                        detailColor = darkMutedSwatch.getRgb();
                                        getWindow().setStatusBarColor(darkVibrantSwatch.getRgb());
                                        play.setTextColor(darkVibrantSwatch.getRgb());
                                        torrents.setTextColor(darkVibrantSwatch.getRgb());
                                        if (status.equals("Movie")) {
                                            darkMutedSwatch = darkVibrantSwatch;
                                        }
                                        getWindow().setNavigationBarColor(darkMutedSwatch.getRgb());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        e.printStackTrace();
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
        getDetails();
    }

    void getDetails() {
        String agent;
        if (status.equals("Movie")) {
            agent = "movie";
        } else {
            agent = "tv";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TMDBApiService service = retrofit.create(TMDBApiService.class);
        service.getDetail("https://api.themoviedb.org/3/" + agent + "/" + tmdbid + "?" +
                "api_key=" + BuildConfig.API_KEY +
                "&append_to_response=external_ids,season%2F1")
                .enqueue(new Callback<MediaDetail>() {
                    @Override
                    public void onResponse(Call<MediaDetail> call, Response<MediaDetail> response) {
                        MediaDetail mediaDetail = response.body();
                        imdb = mediaDetail.getImdb();
                        if (!status.equals("Movie")) {
                            String[] mSeasons = mediaDetail.sNames();
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mSeasons);
                            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            seasoner.setAdapter(arrayAdapter);
                            seasoner.setVisibility(View.VISIBLE);
                            episodesFragment details = new episodesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("color", detailColor);
                            details.setData(mediaDetail.getTvSeason());
                            details.setArguments(bundle);
                            seasoner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        if (flag == 0) {
                                            getSupportFragmentManager().beginTransaction().add(R.id.tvdetails, details, "1").commit();
                                            flag = 1;
                                        } else {
                                            details.setData(mediaDetail.getTvSeason());
                                            if (getFragmentRefreshListener() != null) {
                                                getFragmentRefreshListener().onRefresh();
                                            }
                                        }
                                    } else {
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("http://api.themoviedb.org/3/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        TMDBApiService service = retrofit.create(TMDBApiService.class);
                                        service.getEpisodes(tmdbid, position + 1, BuildConfig.API_KEY).enqueue(new Callback<TVSeason>() {
                                            @Override
                                            public void onResponse(Call<TVSeason> call, Response<TVSeason> response) {
                                                TVSeason tvSeason = response.body();
                                                details.setData(tvSeason);
                                                if (getFragmentRefreshListener() != null) {
                                                    getFragmentRefreshListener().onRefresh();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<TVSeason> call, Throwable t) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                        enableButtons();
                    }

                    @Override
                    public void onFailure(Call<MediaDetail> call, Throwable t) {

                    }
                });
    }

    private void enableButtons() {
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

    public interface FragmentRefreshListener {
        void onRefresh();
    }
}