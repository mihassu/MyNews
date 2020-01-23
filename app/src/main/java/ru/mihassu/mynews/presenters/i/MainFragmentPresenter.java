package ru.mihassu.mynews.presenters.i;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.main.MainFragmentState;

public interface MainFragmentPresenter {
    LiveData<MainFragmentState> subscribe();
    void updateChannels();
    void onFragmentConnected(Observable<List<MyArticle>> searchObservable);
    void onFragmentDisconnected();
}
