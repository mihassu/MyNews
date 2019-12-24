package ru.mihassu.mynews.domain.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.mihassu.mynews.domain.model.MyArticle;

public interface ChannelCollector {
    LiveData<List<MyArticle>> collectChannels();
}
