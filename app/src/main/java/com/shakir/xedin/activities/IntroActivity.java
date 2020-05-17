package com.shakir.xedin.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.shakir.xedin.R;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SliderPage sliderPage = new SliderPage();

        sliderPage.setTitle("Welcome to Xedin");
        sliderPage.setBgColor(Color.parseColor("#10101a"));
        sliderPage.setDescription("Follow this Quick Guide to get Started!");
        sliderPage.setImageDrawable(R.drawable.ic_xed);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        sliderPage.setBgColor(Color.parseColor("#a83a3a"));
        sliderPage.setTitle("Immediate Play");
        sliderPage.setDescription("Select the play button to start direct streaming the media content in a iFrame web module, Hold to select sources.");
        sliderPage.setImageDrawable(R.drawable.playhold);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        sliderPage.setBgColor(Color.parseColor("#008080"));
        sliderPage.setTitle("Torrent Streaming");
        sliderPage.setDescription("For an even higher quality and flexible media streaming, select Torrent Option to search for and stream BitTorrents. External magnet use also available.");
        sliderPage.setImageDrawable(R.drawable.torrent);
        addSlide(AppIntroFragment.newInstance(sliderPage));

//        sliderPage.setBgColor(Color.parseColor("#FF0000"));
//        sliderPage.setTitle("Disclaimer");
//        sliderPage.setDescription("I do not encourage the use of pirated media and no not influence or indulge oneself in doing so, this app is to be only treated as a utility and should not be mass distributed.");
//        sliderPage.setImageDrawable(Color.TRANSPARENT);
//        addSlide(AppIntroFragment.newInstance(sliderPage));

        setZoomAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
