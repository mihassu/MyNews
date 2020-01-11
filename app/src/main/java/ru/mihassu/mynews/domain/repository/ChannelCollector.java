package ru.mihassu.mynews.domain.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;

public interface ChannelCollector {
    LiveData<MainFragmentState> collectChannels();
    void updateChannels();

}
