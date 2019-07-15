package com.shakir.xedin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Sources extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        Intent intent = getIntent();
        //String title = intent.getStringExtra("title");
        int id = intent.getIntExtra("tmdbid", -1);
        int status = intent.getIntExtra("status", -1);
        String season = intent.getStringExtra("season");
        String episode = intent.getStringExtra("episode");
        String loader;
        if (status == 1) {
            loader = "https://videospider.stream/personal?key=" + BuildConfig.SPIDER_KEY + "&video_id=" + id + "&tmdb=1&tv=1&s=" + season + "&e=" + episode;
        } else {
            loader = "https://videospider.stream/personal?key=" + BuildConfig.SPIDER_KEY + "&video_id=" + id + "&tmdb=1";
        }
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(loader));
        startActivity(browse);
//        String line="";
//        Ion.with(getApplicationContext()).load("http://www.ip-api.com/json").asString().setCallback(new FutureCallback<String>() {
//            @Override
//            public void onCompleted(Exception e, String res) {
//                viewer.setText(res);
//            }
//        });
//        a.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String ur = "https://videospider.in/getticket.php" +
//                        "?key=CJvD9WqMGiyVSuu8" +
//                        "&secret_key=nxognyc5j6n4h9tlx6uhdu1y7r5sn7" +
//                        "&video_id=" + id +
//                        "&s=0" +
//                        "&ip="+result.getText();
//                Ion.with(getApplicationContext()).load(ur).asString().setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String res) {
//                        result.setText(res);
//                    }
//                });
//                a.setVisibility(View.GONE);
//                b.setVisibility(View.VISIBLE);
//            }
//        });
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String loadu = "https://videospider.stream/getvideo" +
//                        "?key=CJvD9WqMGiyVSuu8" +
//                        "&video_id=" + id + "&tmdb=1" +
//                        "&ticket=" + result.getText().toString();
//                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(loadu));
//                startActivity(browse);
//            }
//        });

//    private void getWebsite(final String u) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final StringBuilder builder = new StringBuilder();
//
//                try {
//                    Document doc = Jsoup.connect(u).get();
//                    String title = doc.title();
//                    Elements links = doc.select("a[href]");
//
//                    builder.append(title).append("\n");
//
//                    for (Element link : links) {
//                        String li = u+link.attr("href");
//                        if(li.endsWith(".mkv")|| li.endsWith(".mp4") || li.endsWith(".avi")){
//                            builder.append("\n").append("Link : ")
//                                    .append(u)
//                                    .append(link.attr("href"));
//                            a[i] = li;
//                            i++;
//                        }
//                    }
//                } catch (IOException e) {
//                    builder.append("Error : ").append(e.getMessage()).append("\n");
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        result.setText(builder.toString());
//                    }
//                });
//            }
//        }).start();
//    }

        //    public void setDesktopMode(WebView webView,boolean enabled) {
//        String newUserAgent = webView.getSettings().getUserAgentString();
//        if (enabled) {
//            try {
//                String ua = webView.getSettings().getUserAgentString();
//                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
//                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            newUserAgent = null;
//        }
//
//        webView.getSettings().setUserAgentString(newUserAgent);
//        webView.getSettings().setUseWideViewPort(enabled);
//        webView.getSettings().setLoadWithOverviewMode(enabled);
//        webView.reload();
//    }
//    public String getLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
//                        return ip;
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//        }
//        return null;
//    }
    }
}
