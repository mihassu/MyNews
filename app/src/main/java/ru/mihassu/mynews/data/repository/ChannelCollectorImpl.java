package ru.mihassu.mynews.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;

import static ru.mihassu.mynews.Utils.logIt;

public class ChannelCollectorImpl implements ChannelCollector {

    private MutableLiveData<MainFragmentState> liveData = new MutableLiveData<>();
    private BehaviorSubject<Long> manualUpdateToggle;

    @SuppressWarnings("unchecked")
    public ChannelCollectorImpl(List<ChannelRepository> channelRepos, long updateInterval) {

        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();

        for (ChannelRepository channelRepo : channelRepos) {
            observableList.add(channelRepo.updateChannel());
        }

        Observable<Long> periodicUpdateToggle = Observable
                .interval(10, updateInterval * 60, TimeUnit.SECONDS)
                .map(l -> System.currentTimeMillis());

        manualUpdateToggle = BehaviorSubject.createDefault(System.currentTimeMillis());

        Observable<Long> updateTrigger = Observable.combineLatest(
                periodicUpdateToggle,
                manualUpdateToggle,
                Math::max);

        updateTrigger
                .switchMap(millis -> collect(observableList))
                .subscribe(observer);
    }

    @SuppressWarnings("unchecked")
    private Observable<List<MyArticle>> collect(
            List<Observable<List<MyArticle>>> observableList) {

        return Observable.combineLatest(observableList,
                (listOfLists) -> {
                    List<MyArticle> combinedList = new ArrayList<>();

                    for (Object list : listOfLists) {
                        combinedList.addAll((List<MyArticle>) list);
                    }
                    return combinedList;
                })
                .subscribeOn(Schedulers.io());
    }

    private Observer<List<MyArticle>> observer = new Observer<List<MyArticle>>() {
        @Override
        public void onNext(List<MyArticle> myArticles) {

            if (liveData.getValue() != null) {
                MainFragmentState currentState = liveData.getValue();
                currentState.setCurrentArticles(myArticles);
                liveData.postValue(currentState);
            } else {
                liveData.postValue(new MainFragmentState(myArticles));
            }
        }

        @Override
        public void onError(Throwable e) {
            logIt("Channel Collector error");
        }

        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onComplete() {
        }
    };

    @Override
    public LiveData<MainFragmentState> collectChannels() {
        return liveData;
    }

    @Override
    public void updateChannels() {
        manualUpdateToggle.onNext(System.currentTimeMillis());
    }
}
