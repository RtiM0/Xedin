package com.shakir.xedin.fragments;

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.michaelflisar.changelog.ChangelogBuilder;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial;
import com.shakir.xedin.R;

public class AboutFragment extends MaterialAboutFragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    protected MaterialAboutList getMaterialAboutList(Context activityContext) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Xedin")
                .desc("Stream Movies/TV")
                .icon(R.mipmap.ic_xed)
                .build());
        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(getContext(),
                new IconicsDrawable(getContext())
                        .icon(CommunityMaterial.Icon.cmd_clock_outline),
                "Version",
                true));
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(new IconicsDrawable(getContext())
                        .icon(CommunityMaterial.Icon.cmd_cellphone_arrow_down))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        new ChangelogBuilder()
                                .withTitle("Xedin Changelog")
                                .withOkButtonLabel("Yeah Yeah")
                                .withUseBulletList(true)
                                .buildAndShowDialog((AppCompatActivity) getContext(), true);
                    }
                })
                .build());
        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("RtiM0")
                .subText("India")
                .icon(new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_account_badge_outline))
                .build());
        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Services Used");

        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(getContext(),
                new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_google_physical_web),
                "TMDB",
                false,
                Uri.parse("themoviedb.org")));
//        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(getContext(),
//                new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_github_face),
//                "TorrentStream",
//                false,
//                Uri.parse("https://github.com/TorrentStream/TorrentStream-Android")));
//        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(getContext(),
//                new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_github_face),
//                "Changelog",
//                false,
//                Uri.parse("https://github.com/MFlisar/changelog")));

        return new MaterialAboutList.Builder()
                .addCard(appCardBuilder.build())
                .addCard(authorCardBuilder.build())
                .addCard(convenienceCardBuilder.build())
                .build();
    }
}
