package ru.mihassu.mynews.ui.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }
}