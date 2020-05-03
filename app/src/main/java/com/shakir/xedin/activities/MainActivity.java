package com.shakir.xedin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.michaelflisar.changelog.ChangelogBuilder;
import com.shakir.xedin.R;
import com.shakir.xedin.fragments.AboutFragment;
import com.shakir.xedin.fragments.PopularMovies;
import com.shakir.xedin.fragments.PopularTV;
import com.shakir.xedin.fragments.Search;

public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new PopularMovies();
    final Fragment fragment2 = new PopularTV();
    final Fragment fragment3 = new Search();
    final Fragment fragment4 = new AboutFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return true;
            case R.id.navigation_dashboard:
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return true;
            case R.id.navigation_notifications:
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                return true;
            case R.id.navigation_about:
                fm.beginTransaction().hide(active).show(fragment4).commit();
                active = fragment4;
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayOptions
                    (ActionBar.DISPLAY_SHOW_CUSTOM);
            actionbar.setCustomView(R.layout.action_bar);
            actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10101a")));
        }
        getWindow().setNavigationBarColor(Color.parseColor("#10101a"));
        ChangelogBuilder builder = new ChangelogBuilder()
                .withTitle("Xedin Changelog")
                .withOkButtonLabel("Yeah Yeah")
                .withUseBulletList(true);
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Intent intro = new Intent(this, IntroActivity.class);
            startActivity(intro);
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();
        builder.withManagedShowOnStart(true).buildAndShowDialog(this, true);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
    }
}