package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;

import ru.mihassu.mynews.ui.Fragments.MainFragmentState;

public interface MainFragmentPresenter {
    LiveData<MainFragmentState> subscribe();
    void updateChannels();
}
