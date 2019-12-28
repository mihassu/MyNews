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

import static ru.mihassu.mynews.Utils.logIt;

public class ChannelCollectorImpl implements ChannelCollector {

    private MutableLiveData<List<MyArticle>> liveData = new MutableLiveData<>();
    private BehaviorSubject<Long> manualUpdateToggle;

    @SuppressWarnings("unchecked")
    public ChannelCollectorImpl(List<ChannelRepository> channelRepos, long updateInterval) {

        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();

        for (ChannelRepository channelRepo : channelRepos) {
            observableList.add(channelRepo.updateChannel());
        }

        Observable<Long> periodicUpdateToggle = Observable
                .interval(0, updateInterval, TimeUnit.MINUTES)
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
            liveData.postValue(myArticles);
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
    public LiveData<List<MyArticle>> collectChannels() {
        return liveData;
    }

    @Override
    public void updateChannels() {
        manualUpdateToggle.onNext(System.currentTimeMillis());
    }
}
