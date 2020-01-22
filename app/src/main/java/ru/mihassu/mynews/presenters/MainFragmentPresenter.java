package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;

public interface MainFragmentPresenter {
    LiveData<MainFragmentState> subscribe();
    void updateChannels();
    void fragmentConnected(Observable<List<MyArticle>> searchObservable);
    void fragmentDisconnected();
}
