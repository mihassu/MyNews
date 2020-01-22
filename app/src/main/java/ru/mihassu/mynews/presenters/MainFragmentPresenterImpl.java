package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;
import ru.mihassu.mynews.ui.main.BookmarkChangeListener;

import static ru.mihassu.mynews.Utils.logIt;

public class MainFragmentPresenterImpl implements MainFragmentPresenter, ArticlePresenter {

    private MutableLiveData<MainFragmentState> liveData = new MutableLiveData<>();

    private RoomRepoBookmark roomRepoBookmark;
    private ChannelCollector collector;
    private BookmarkChangeListener listener;

    public MainFragmentPresenterImpl(
            RoomRepoBookmark roomRepoBookmark,
            ChannelCollector collector) {

        this.roomRepoBookmark = roomRepoBookmark;
        this.collector = collector;
        subscribeToDataSources();
    }

    /**
     * -------------------------------------
     * MainFragmentPresenter implementation
     * -------------------------------------
     */

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

        // Итоговоый список статей отсортировать по дате
        ArrayList<MyArticle> result = new ArrayList<>(map.values());
        Collections.sort(result);

        return result;
    }

    /**
     * -------------------------------
     * ArticlePresenter implementation
     * -------------------------------
     */

    @Override
    public void onClickBookmark(long articleId) {
        MainFragmentState currentState = liveData.getValue();

        if (currentState != null) {

            // Найти статью в общем списке по её ID
            MyArticle article = currentState
                    .getCurrentArticles()
                    .stream()
                    .filter((a) -> a.id == articleId)
                    .collect(Collectors.toList())
                    .get(0);

            // Обновить базу
            if (article.isMarked) {
                roomRepoBookmark.insertArticle(article);
            } else {
                roomRepoBookmark.deleteArticle(article);
            }

            // Определить положение статьи в RecyclerView
            List<MyArticle> list = currentState.getCurrentSortedArticles().get(article.category);

            if (list != null) {
                listener.onItemUpdated(list.indexOf(article));
            }
        }
    }

    @Override
    public void onClickArticle(long articleId) {

    }

    @Override
    public void bindBookmarkChangeListener(BookmarkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public List<MyArticle> getArticle(int listPosition) {
        return null;
    }

    /**
     * Выдать список статей согласно индексу таба (отдельная категория статей)
     */
    @Override
    public List<MyArticle> getTabArticles(int tabPosition) {

        if (liveData.getValue() != null) {
            MainFragmentState currentState = liveData.getValue();

            EnumMap<ArticleCategory, List<MyArticle>> classifiedNews = currentState.getCurrentSortedArticles();

            if (classifiedNews != null && classifiedNews.size() != 0) {
                ArrayList<Map.Entry<ArticleCategory, List<MyArticle>>> allArticles =
                        new ArrayList<>(classifiedNews.entrySet());

                return allArticles.get(tabPosition).getValue();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Выдать все статьи
     */
    @Override
    public List<MyArticle> getArticles() {
        if (liveData.getValue() != null) {
            MainFragmentState currentState = liveData.getValue();
            return currentState.getCurrentArticles();
        }
        return new ArrayList<>();
    }

    @Override
    public int getTabCount() {
        if (liveData.getValue() != null) {
            MainFragmentState currentState = liveData.getValue();
            return currentState.getCurrentSortedArticles().keySet().size();
        }

        return 0;
    }
}
