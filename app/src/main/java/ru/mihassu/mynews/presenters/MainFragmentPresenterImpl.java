package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;

import static ru.mihassu.mynews.Utils.logIt;

public class MainFragmentPresenterImpl implements MainFragmentPresenter {

    private MutableLiveData<MainFragmentState> liveData = new MutableLiveData<>();

    private RoomRepoBookmark roomRepoBookmark;
    private ChannelCollector collector;

    public MainFragmentPresenterImpl(RoomRepoBookmark roomRepoBookmark,
                                     ChannelCollector collector) {
        this.roomRepoBookmark = roomRepoBookmark;
        this.collector = collector;
        subscribeToDataSources();
    }

    /**
     * Подписаться на данные из коллектора и список bookmark'ов из базы
     */
    private void subscribeToDataSources() {
        Observable.combineLatest(
                collector.collectChannels(),
                roomRepoBookmark.getArticles(),
                this::makeBookmark
        ).subscribe(new DisposableObserver<List<MyArticle>>() {
            @Override
            public void onNext(List<MyArticle> list) {
                if (liveData.getValue() != null) {
                    MainFragmentState currentState = liveData.getValue();
                    currentState.setCurrentArticles(list);
                    liveData.postValue(currentState);
                } else {
                    liveData.postValue(new MainFragmentState(list));
                }
            }

            @Override
            public void onError(Throwable e) {
                logIt("error in subscribeToDataSources method");
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
        collector.updateChannels();
    }

    // Проставить Bookmark
    private List<MyArticle> makeBookmark(List<MyArticle> allArticles,
                                         List<MyArticle> markedArticles) {

        // List allArticles в synchronized HashMap
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

        ArrayList<MyArticle> result = new ArrayList<>(map.values());
        Collections.sort(result);

        return result;
    }
}
