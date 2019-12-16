package ru.mihassu.mynews.ui.web;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import ru.mihassu.mynews.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();
        String url = intent.getStringExtra(getString(R.string.article_url_key));
        showContent(mobileUrl(url));
    }

    private String mobileUrl(String url) {
        String result = url;
        if(!url.contains("//m.")){
            result = url.replace("//", "//m.");
        }
        return result;
    }

    private void showContent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        builder.setToolbarColor(Color.CYAN);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
