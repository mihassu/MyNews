package ru.mihassu.mynews.presenters.i;

import androidx.lifecycle.LiveData;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.DataSnapshot;
import ru.mihassu.mynews.ui.fragments.main.MainFragmentState;

public interface MainFragmentPresenter {
    LiveData<MainFragmentState> subscribe();
    void updateChannels();
    void onFragmentConnected(Observable<DataSnapshot> searchObservable);
    void onFragmentDisconnected();
}
