package com.shakir.xedin.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.shakir.xedin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Authored by RtiM0 (shakirmustafa58@gmail.com)
 */

public class WebScrap extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> sad = new ArrayList<>();
    TextView titleText;
    int source1size = 0;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_scrap);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Getting links");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        listView = findViewById(R.id.resu);
        String name = getIntent().getStringExtra("name");
        String season = getIntent().getStringExtra("season");
        String episode = getIntent().getStringExtra("episode");
        String title = name + " Season " + season + " Episode " + episode;
        titleText = findViewById(R.id.title2);
        titleText.setText(title);
        String query = name + " s" + season + " e" + episode;
        query = query.replace(" ", "_");
        query = query.toLowerCase();
        String finalQuery = query;
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www1.swatchseries.to/episode/" + finalQuery + ".html").get();
                Elements clipwatching = doc.getElementsByClass("download_link_clipwatching.com ");
                Elements abcvideo = doc.getElementsByClass("download_link_abcvideo.cc ");
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
                source1size = arrayList.size();
                for (Element link : abcvideo) {
                    String pre = link.getElementsByClass("deletelinks").get(0).html().substring(65);
                    int suf = pre.indexOf("'))");
                    String lenk = pre.substring(0, suf);
                    arrayList.add(lenk);
                    sad.add("Name not found");
                }
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
                titleText.setText("No result found");
            }
            runOnUiThread(this::buildList);
        }).start();
    }

    private void buildList() {
        if (arrayList.isEmpty()) {
            titleText.setText("NO RESULTS FOUND");
        } else {
            Snackbar.make(findViewById(R.id.mainContent), "Tap and hold \"name not found\" links to fetch their names", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CLOSE", view -> {

                    })
                    .show();
        }
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.torrent_list_items, R.id.textV, sad));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String link = arrayList.get(position);
            dialog.setTitle("Preparing Stream");
            dialog.show();
            new Thread(() -> {
                try {
                    Document doc = Jsoup.connect(link).get();
                    String html = doc.html();
                    Pattern pattern = Pattern.compile("(http?s:.*?(mp4|m3u8))");
                    Matcher matcher = pattern.matcher(html);
                    if (matcher.find()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(matcher.group(1)), "video/*");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    runOnUiThread(() -> Toast.makeText(WebScrap.this, "Cant find link", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, "Fetching Name", Toast.LENGTH_SHORT).show();
            String link = arrayList.get(i);
            new Thread(() -> {
                try {
                    Document doc = Jsoup.connect(link).get();
                    runOnUiThread(() -> ((TextView) view.findViewById(R.id.textV)).setText((doc.title()).substring(5)));
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Cant find name", Toast.LENGTH_SHORT).show());
                }
            }).start();
            return true;
        });
    }
}
