package ru.mihassu.mynews.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

import static ru.mihassu.mynews.Utils.logIt;

public class ChannelCollectorImpl implements ChannelCollector {

    private MutableLiveData<List<MyArticle>> liveData = new MutableLiveData<>();

    private Observable<Long> triggerOnInterval;
    private BehaviorSubject<Long> triggerOnEvent;
    private Observable<Long> updateTrigger;

    public ChannelCollectorImpl(List<ChannelRepository> channelRepos, long updateInterval) {

        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();

        for (ChannelRepository channelRepo : channelRepos) {
            observableList.add(channelRepo.updateChannel());
        }

        triggerOnInterval = Observable
                .interval(0, updateInterval, TimeUnit.MINUTES)
                .map(l -> System.currentTimeMillis());

        triggerOnEvent = BehaviorSubject.createDefault(System.currentTimeMillis());

        updateTrigger = Observable.combineLatest(
                triggerOnInterval,
                triggerOnEvent,
                Math::max);

        updateTrigger
                .switchMap(millis ->
                        Observable.combineLatest(observableList,
                                (listOfLists) -> {

                                    List<MyArticle> combinedList = new ArrayList<>();

                                    for (Object list : listOfLists) {
                                        combinedList.addAll((List<MyArticle>) list);
                                    }
                                    return combinedList;
                                })
                                .subscribeOn(Schedulers.io())


                )
                .doOnNext(list -> logIt("onNext list size = " + list.size()))
                .doOnComplete(() -> logIt("onComplete"))
                .subscribe(
                        list -> {
                            liveData.postValue(list);
                        },
                        error -> {
                            logIt("Channel Collector error");
                        }
                );
    }


//    public ChannelCollectorImpl(List<ChannelRepository> channelRepos) {
//
//        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();
//
//        for (ChannelRepository channelRepo : channelRepos) {
//            observableList.add(channelRepo.getChannel());
//        }
//
//        // Получить данные из всех созданных Observable'ов в виде
//        // списка списков List<List<MyArticle>> и превратить его в
//        // плоский список, в котором данные из всех RSS.
//        Observable.combineLatest(observableList,
//                (listOfLists) -> {
//
//                    List<MyArticle> combinedList = new ArrayList<>();
//
//                    for (Object list : listOfLists) {
//                        combinedList.addAll((List<MyArticle>) list);
//                    }
//                    return combinedList;
//                })
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                        list -> {
//                            liveData.postValue(list);
//                        },
//                        error -> {
//                            logIt("Channel Collector error");
//                        }
//                );
//    }


    @Override
    public LiveData<List<MyArticle>> collectChannels() {
        return liveData;
    }

    @Override
    public void updateChannels() {
        triggerOnEvent.onNext(System.currentTimeMillis());
    }
}
