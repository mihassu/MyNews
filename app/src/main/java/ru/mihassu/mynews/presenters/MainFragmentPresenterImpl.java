package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
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

//        Observable<List<MyArticle>> aaa = Observable.combineLatest(
//                collector.collectChannelsRx(),
//                roomRepoBookmark.getArticlesRx(),
//                new BiFunction<List<MyArticle>, List<MyArticle>, List<MyArticle>>() {
//                    @Override
//                    public List<MyArticle> apply(List<MyArticle> myArticles, List<MyArticle> myArticles2) throws Exception {
//                        return null;
//                    }
//                }
//
//        );

        Observable<List<MyArticle>> aaa = Observable.combineLatest(
                collector.collectChannelsRx(),
                roomRepoBookmark.getArticlesRx(),
                this::makeBookmark
        );

        Disposable d = aaa.subscribe((list) -> {
                    if (liveData.getValue() != null) {
                        MainFragmentState currentState = liveData.getValue();
                        currentState.setCurrentArticles(list);
                        liveData.postValue(currentState);
                    } else {
                        liveData.postValue(new MainFragmentState(list));
                    }
                },
                (error) -> {
                    logIt("ERROR in FragmentPresenter");
                });
    }

    @Override
    public LiveData<MainFragmentState> subscribe() {
        return liveData;
    }

    // Проставить Bookmark
    private List<MyArticle> makeBookmark(List<MyArticle> all, List<MyArticle> marked) {

        // List в synchronized HashMap
        Map<Long, MyArticle> map =
                Collections.synchronizedMap(
                        all
                                .stream()
                                .collect(Collectors.toMap(a -> a.id, a -> a)));

        for (MyArticle markedArticle : marked) {
            long key = markedArticle.id;

            if (map.containsKey(key)) {
                MyArticle a = map.get(key);
                if (a != null) {
                    a.isMarked = true;
                    map.replace(key, a);
                }
            }
        }

        return new ArrayList<>(map.values());
    }
}
