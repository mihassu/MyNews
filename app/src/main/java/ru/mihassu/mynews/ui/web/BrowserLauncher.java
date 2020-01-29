package ru.mihassu.mynews.ui.web;

public interface BrowserLauncher {
    int requestCode = 100;
    void showInBrowser(String url);
}
