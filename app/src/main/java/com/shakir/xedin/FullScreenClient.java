package com.shakir.xedin;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;

public class FullScreenClient extends WebChromeClient {
    private View customview;
    private ViewGroup parent;
    private ViewGroup content;

    public FullScreenClient(ViewGroup parent, ViewGroup content) {
        this.parent = parent;
        this.content = content;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        customview = view;
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.addView(view);
        content.setVisibility(View.GONE);
    }

    @Override
    public void onHideCustomView() {
        content.setVisibility(View.VISIBLE);
        parent.removeView(customview);
        customview = null;
    }
}
