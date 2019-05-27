package com.shakir.xedin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoPage extends AppCompatActivity {

    private RelativeLayout backs;
    private TextView tet;
    private ImageView backd;
    private ImageView post;
    private TextView disc;
    private Button play;
    private EditText season;
    private EditText episode;
    private Button alternate;
    private Button odb;
    private String status;
    private int tmdbid;

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
        post = findViewById(R.id.postar);
        disc = findViewById(R.id.description);
        backs = findViewById(R.id.backs);
        play = findViewById(R.id.playbutton);
        alternate = findViewById(R.id.alternate);
        odb = findViewById(R.id.odb);
        season = findViewById(R.id.season);
        episode = findViewById(R.id.episode);
        if (name == null) {
            tet.setText(title);
            status = "Movie";
        } else {
            tet.setText(name);
            status = "TV";
            season.setVisibility(View.VISIBLE);
            episode.setVisibility(View.VISIBLE);
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
                                        } catch (NullPointerException e) {

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
        if (status == "Movie") {
            imdbSources("https://api.themoviedb.org/3/movie/" + tmdbid + "?api_key=" + BuildConfig.API_KEY);
        } else {
            imdbSources("http://api.themoviedb.org/3/tv/" + tmdbid + "?api_key=" + BuildConfig.API_KEY + "&append_to_response=external_ids");
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loader;
                if (status == "TV") {
                    loader = "https://videospider.stream/personal?key=" + BuildConfig.SPIDER_KEY + "&video_id=" + tmdbid + "&tmdb=1&tv=1&s=" + season.getText().toString() + "&e=" + episode.getText().toString();
                } else {
                    loader = "https://videospider.stream/personal?key=" + BuildConfig.SPIDER_KEY + "&video_id=" + tmdbid + "&tmdb=1";
                }
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(loader));
                startActivity(browse);
//                Intent intent = new Intent(getApplicationContext(),Sources.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("tmdbid",tmdbid);
//                if(status=="TV"){
//                    intent.putExtra("status",1);
//                    intent.putExtra("season",season.getText().toString());
//                    intent.putExtra("episode",episode.getText().toString());
//                }
//                getApplicationContext().startActivity(intent);
            }
        });
    }

    void imdbSources(String url) {
        Ion.with(getApplicationContext())
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String imdb = "";
                            if (status == "Movie") {
                                imdb = jsonObject.getString("imdb_id");
                                alternate.setVisibility(View.VISIBLE);
                                final String finalImdb1 = imdb;
                                alternate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(), "Scroll Down", Toast.LENGTH_LONG).show();
                                        Intent alternate = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vplus.ucoz.com/" + finalImdb1));
                                        startActivity(alternate);
                                    }
                                });
                            } else {
                                JSONObject object = new JSONObject(jsonObject.getString("external_ids"));
                                imdb = object.getString("imdb_id");
                            }
                            odb.setVisibility(View.VISIBLE);
                            final String finalImdb = imdb;
                            odb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (status == "TV") {
                                        Intent odb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.odb.to/embed?imdb_id=" + finalImdb + "&s=" + season.getText().toString() + "&e=" + episode.getText().toString() + "&cc=eng"));
                                        startActivity(odb);
                                        openURL("https://api.odb.to/embed?imdb_id=" + finalImdb + "&s=" + season.getText().toString() + "&e=" + episode.getText().toString() + "&cc=eng");
                                    } else {
                                        Intent odb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.odb.to/embed?imdb_id=" + finalImdb + "&cc=eng"));
                                        startActivity(odb);
                                    }
                                }
                            });
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    void openURL(String irl) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.build()
                .launchUrl(this, Uri.parse(irl));
    }
}