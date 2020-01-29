package ru.mihassu.mynews.data.eventbus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.model.DataSnapshot;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;

import static ru.mihassu.mynews.Utils.logIt;

public class ActualDataBusImp implements ActualDataBus {

    private RoomRepoBookmark roomRepoBookmark;
    private ChannelCollector collector;
    private Subject<DataSnapshot> dataPrePublisher;
    private BehaviorSubject<DataSnapshot> dataPublisher;
    private BehaviorSubject<List<MyArticle>> bookmarkPublisher;

    public ActualDataBusImp(RoomRepoBookmark roomRepoBookmark,
                            ChannelCollector collector,
                            Subject<DataSnapshot> dataPrePublisher,
                            BehaviorSubject<DataSnapshot> dataPublisher,
                            BehaviorSubject<List<MyArticle>> bookmarkPublisher) {

        this.roomRepoBookmark = roomRepoBookmark;
        this.collector = collector;
        this.dataPrePublisher = dataPrePublisher;
        this.dataPublisher = dataPublisher;
        this.bookmarkPublisher = bookmarkPublisher;
        subscribeToDataSources();
    }

    @Override
    public Observable<DataSnapshot> connectToActualData() {
        return dataPublisher.hide();
    }

    @Override
    public Observable<List<MyArticle>> connectToBookmarkData() {
        return bookmarkPublisher.hide();
    }

    @Override
    public void updateActualData() {
        collector.updateChannels();
    }

    @Override
    public void broadcastSearchResult(DataSnapshot dataSnapshot) {
        dataPrePublisher.onNext(dataSnapshot);
    }

    /**
     * Подписаться на данные из коллектора и таблицу bookmark'ов из базы
     */
    private void subscribeToDataSources() {

        // From Bookmarks
        roomRepoBookmark
                .getBookmarkedArticles()
                .subscribe(bookmarkPublisher);

        // From Collector (map List to Snapshot)
        collector
                .collectChannels()
                .map(list -> new DataSnapshot(list, NO_HIGHLIGHT))
                .subscribe(dataPrePublisher);

        Observable.combineLatest(
                dataPrePublisher,
                bookmarkPublisher,
                this::setBookmarksEx
        ).subscribe(new DisposableObserver<DataSnapshot>() {
            @Override
            public void onNext(DataSnapshot dataSnapshot) {
                dataPublisher.onNext(dataSnapshot);
            }

            @Override
            public void onError(Throwable e) {
                logIt("ADB error\n" + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    // Проставить Bookmark
    private DataSnapshot setBookmarksEx(DataSnapshot dataSnapshot,
                                        List<MyArticle> markedArticles) {

        List<MyArticle> allArticles = dataSnapshot.getArticles();

        // Сделать КОПИЮ оригинального списка и поместить его в synchronized HashMap
        Map<Long, MyArticle> map =
                Collections.synchronizedMap(
                        (new ArrayList<>(allArticles))
                                .stream()
                                .collect(Collectors.toMap(a -> a.id,
                                        a -> {
                                            a.isMarked = false;
                                            return a;
                                        })));

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

        return new DataSnapshot(result, dataSnapshot.getQuery());
    }


    // Проставить Bookmark
    private List<MyArticle> setBookmarks(List<MyArticle> allArticles,
                                         List<MyArticle> markedArticles) {

        // Сделать КОПИЮ оригинального списка и поместить его в synchronized HashMap
        Map<Long, MyArticle> map =
                Collections.synchronizedMap(
                        (new ArrayList<>(allArticles))
                                .stream()
                                .collect(Collectors.toMap(a -> a.id,
                                        a -> {
                                            a.isMarked = false;
                                            return a;
                                        })));

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
