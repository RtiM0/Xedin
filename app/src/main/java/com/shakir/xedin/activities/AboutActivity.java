package com.shakir.xedin.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.shakir.xedin.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_launcher_foreground)
                .setDescription("A light application that retrieves data from TMDB, with various media streaming features")
                .addItem(new Element().setTitle("Version 0.9.5a"))
//                .addItem(new Element().setTitle("Made with <3 by Mustafa Shakir (RtiM0)"))
//                .addItem(new Element().setTitle("This version is and can only be distributed Manish Boddhana"))
//                .addGroup("Connect")
//                .addEmail("shakirmustafa58@gmail.com", "Mail")
//                .addTwitter("RtiM0", "Tweet")
//                .addGitHub("RtiM0", "Github")
                .create();
        setContentView(aboutPage);
    }
}
