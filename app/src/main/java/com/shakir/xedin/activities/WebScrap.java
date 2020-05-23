package com.shakir.xedin.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.shakir.xedin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScrap extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> sad = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_scrap);
        listView = findViewById(R.id.resu);
        String name = getIntent().getStringExtra("name");
        String season = getIntent().getStringExtra("season");
        String episode = getIntent().getStringExtra("episode");
        String title = name + " Season " + season + " Episode " + episode;
        sad.add(title);
        String query = name + " s" + season + " e" + episode;
        query = query.replace(" ", "_");
        query = query.toLowerCase();
        arrayList.add("BUFFER");
        String finalQuery = query;
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www1.swatchseries.to/episode/" + finalQuery + ".html").get();
                Elements clipwatching = doc.getElementsByClass("download_link_clipwatching.com ");
                for (Element link : clipwatching) {
                    String pre = link.getElementsByClass("deletelinks").get(0).html().substring(65);
                    int suf = pre.indexOf("'))");
                    String lenk = pre.substring(0, suf);
                    arrayList.add(lenk);
                    try {
                        String fo = lenk.split("/")[4];
                        Pattern reg = (Pattern.compile("((.*?)(mkv|mp4|avi))"));
                        Matcher matcher = reg.matcher(fo);
                        if (matcher.find()) {
                            sad.add(matcher.group(1));
                        } else {
                            sad.add("Name not found");
                        }
                    } catch (Exception e) {
                        sad.add("Name not found");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(this::buildList);
        }).start();

    }

    private void buildList() {
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.torrent_list_items, R.id.textV, sad));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String link = arrayList.get(position);
            new Thread(() -> {
                try {
                    Document doc = Jsoup.connect(link).get();
                    String html = doc.html();
                    Pattern pattern = Pattern.compile("(http?s:.*?(mp4|m3u8))");
                    Matcher matcher = pattern.matcher(html);
                    if (matcher.find()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(matcher.group(1)));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
