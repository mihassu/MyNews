package ru.mihassu.mynews.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;

import static ru.mihassu.mynews.Utils.logIt;

public class ActualDataBusImp implements ActualDataBus {

    private RoomRepoBookmark roomRepoBookmark;
    private ChannelCollector collector;
    private BehaviorSubject<List<MyArticle>> publisher;

    public ActualDataBusImp(RoomRepoBookmark roomRepoBookmark,
                            ChannelCollector collector,
                            BehaviorSubject<List<MyArticle>> publisher) {

        this.roomRepoBookmark = roomRepoBookmark;
        this.collector = collector;
        this.publisher = publisher;
        subscribeToDataSources();
    }

    @Override
    public Observable<List<MyArticle>> connectToActualData() {
        return publisher.hide();
    }

    @Override
    public void updateActualData() {
        collector.updateChannels();
    }

    @Override
    public void broadcastSearchResult(List<MyArticle> searchResult) {
        publisher.onNext(searchResult);
    }

    /**
     * Подписаться на данные из коллектора и таблицу bookmark'ов из базы
     */
    private void subscribeToDataSources() {
        Observable.combineLatest(
                collector.collectChannels(),
                roomRepoBookmark.getArticles(),
                this::setBookmarks
        ).subscribe(new DisposableObserver<List<MyArticle>>() {
            @Override
            public void onNext(List<MyArticle> list) {
                publisher.onNext(list);
            }

            @Override
            public void onError(Throwable e) {
                logIt("error in DataBus");
            }

            @Override
            public void onComplete() {
            }
        });
    }

    // Проставить Bookmark
    private List<MyArticle> setBookmarks(List<MyArticle> allArticles,
                                         List<MyArticle> markedArticles) {

        // Поместить List allArticles в synchronized HashMap
        Map<Long, MyArticle> map =
                Collections.synchronizedMap(
                        allArticles
                                .stream()
                                .collect(Collectors.toMap(a -> a.id, a -> a)));

        for (MyArticle markedArticle : markedArticles) {
            long key = markedArticle.id;

            if (map.containsKey(key)) {
                MyArticle a = map.get(key);
                if (a != null) {
                    a.isMarked = true;
                    map.replace(key, a);
                }
            }
        }

        // Итоговый список статей отсортировать по дате
        ArrayList<MyArticle> result = new ArrayList<>(map.values());
        Collections.sort(result);

        return result;
    }
}
