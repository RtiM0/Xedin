package com.shakir.xedin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.shakir.xedin.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        return new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.ic_launcher_foreground)
                .setDescription("A light application that retrieves data from TMDB, with various media streaming features")
                .addItem(new Element().setTitle("Version 0.9.6"))
                .addItem(new Element().setTitle("Made with <3 by Mustafa Shakir (RtiM0)"))
                .addGroup("Connect")
                .addEmail("shakirmustafa58@gmail.com", "Mail")
                .addTwitter("RtiM0", "Tweet")
                .addGitHub("RtiM0", "Github")
                .create();
    }
}
