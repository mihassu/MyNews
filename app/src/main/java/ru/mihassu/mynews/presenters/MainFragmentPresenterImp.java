package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ru.mihassu.mynews.data.eventbus.ActualDataBus;
import ru.mihassu.mynews.domain.model.DataSnapshot;
import ru.mihassu.mynews.presenters.i.MainFragmentPresenter;
import ru.mihassu.mynews.ui.fragments.main.MainFragmentState;

import static ru.mihassu.mynews.Utils.logIt;

public class MainFragmentPresenterImp implements MainFragmentPresenter {

    private ActualDataBus dataBus;

    private MutableLiveData<MainFragmentState> liveData = new MutableLiveData<>();
    private Disposable searchDisposable;

    public MainFragmentPresenterImp(ActualDataBus dataBus) {
        this.dataBus = dataBus;
        subscribeToDataSources();
    }

    private void subscribeToDataSources() {
        dataBus
                .connectToActualData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<DataSnapshot>() {
                    @Override
                    public void onNext(DataSnapshot dataSnapshot) {
                        if (liveData.getValue() != null) {
                            MainFragmentState currentState = liveData.getValue();
                            currentState.updateState(dataSnapshot);

                            liveData.setValue(currentState);
                        } else {
                            liveData.setValue(new MainFragmentState(dataSnapshot));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        logIt("MFP: error in subscribe\n" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public LiveData<MainFragmentState> subscribe() {
        return liveData;
    }

    @Override
    public void updateChannels() {
        dataBus.updateActualData();
    }

    @Override
    public void onFragmentConnected(Observable<DataSnapshot> searchObservable) {
        searchDisposable = searchObservable
                .subscribe(
                        snapshort -> dataBus.broadcastSearchResult(snapshort)
                );
    }

    @Override
    public void onFragmentDisconnected() {
        if (searchDisposable != null && !searchDisposable.isDisposed()) {
            searchDisposable.dispose();
        }
    }
}
