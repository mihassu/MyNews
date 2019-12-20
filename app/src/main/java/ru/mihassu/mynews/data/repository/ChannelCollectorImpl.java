package ru.mihassu.mynews.data.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

public class ChannelCollectorImpl implements ChannelCollector {

    private List<ChannelRepository> channelRepos;

    public ChannelCollectorImpl(List<ChannelRepository> channelRepos) {
        this.channelRepos = channelRepos;
    }

    @Override
    public Single<List<MyArticle>> collectChannels() {

        List<Observable<List<MyArticle>>> observableList = new ArrayList<>();

        for(ChannelRepository channelRepo : channelRepos) {
            observableList.add(channelRepo.getChannel().toObservable());
        }

        // Получить данные из всех созданных Observable'ов в виде
        // списка списков List<List<MyArticle>> и превратить его в
        // плоский список, в котором данные из всех RSS.
        return Observable.combineLatest(observableList,
                        (listOfLists) -> {

                            List<MyArticle> combinedList = new ArrayList<>();

                            for (Object list : listOfLists) {
                                combinedList.addAll((List<MyArticle>) list);
                            }
                            return combinedList;
                        }
                )
                .firstOrError()
                .subscribeOn(Schedulers.io());
    }
}
