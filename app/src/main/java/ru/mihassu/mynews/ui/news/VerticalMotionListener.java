package ru.mihassu.mynews.ui.news;

public interface VerticalMotionListener {
    float minStartOffset = 0f;

    void onOffsetChanged(float offset);
    void onUpdateMaxOffset(float maxOffset);
    void setDebugFlag(boolean flag);
    void showProgress(float offset);
}
