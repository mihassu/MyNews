package ru.mihassu.mynews.data.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

public class ChannelCollectorImpl implements ChannelCollector {

    private List<ChannelRepository> channelRepos;
    private BehaviorSubject<List<MyArticle>> collectedData = BehaviorSubject.create();
    private Observable<List<MyArticle>> stream;

    public ChannelCollectorImpl(List<ChannelRepository> channelRepos) {
        this.channelRepos = channelRepos;

        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();

        for (ChannelRepository channelRepo : channelRepos) {
            observableList.add(channelRepo.getChannelObs());
        }

        // Получить данные из всех созданных Observable'ов в виде
        // списка списков List<List<MyArticle>> и превратить его в
        // плоский список, в котором данные из всех RSS.
        stream = Observable.combineLatest(observableList,
                (listOfLists) -> {

                    List<MyArticle> combinedList = new ArrayList<>();

                    for (Object list : listOfLists) {
                        combinedList.addAll((List<MyArticle>) list);
                    }
                    return combinedList;
                })
                .subscribeOn(Schedulers.io());

        stream.subscribeWith(collectedData);
    }


    @Override
    public Observable<List<MyArticle>> collectChannels() {
        return collectedData.hide();

//        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();
//
//        for (ChannelRepository channelRepo : channelRepos) {
//            observableList.add(channelRepo.getChannel().toObservable());
//        }
//
//        // Получить данные из всех созданных Observable'ов в виде
//        // списка списков List<List<MyArticle>> и превратить его в
//        // плоский список, в котором данные из всех RSS.
//        return Observable.combineLatest(observableList,
//                (listOfLists) -> {
//
//                    List<MyArticle> combinedList = new ArrayList<>();
//
//                    for (Object list : listOfLists) {
//                        combinedList.addAll((List<MyArticle>) list);
//                    }
//                    return combinedList;
//                }
//        )
//                .firstOrError()
//                .subscribeOn(Schedulers.io());
    }
}
