package ru.mihassu.mynews.presenters;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.observers.DisposableObserver;
import ru.mihassu.mynews.data.ActualDataBus;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;
import ru.mihassu.mynews.ui.main.BookmarkChangeListener;

import static ru.mihassu.mynews.Utils.logIt;

public class RegularArticlePresenter extends BasePresenter implements ArticlePresenter {

    private RoomRepoBookmark repoBookmark;
    private MainFragmentState currentState;
    private BookmarkChangeListener listener;  // должен быть адаптер списка

    public RegularArticlePresenter(ActualDataBus dataBus, RoomRepoBookmark repoBookmark) {
        super(dataBus);
        this.repoBookmark = repoBookmark;
        subscribeToDataSources();
    }

    @Override
    void subscribeToDataSources() {
        dataBus
                .connectToActualData()
                .subscribe(new DisposableObserver<List<MyArticle>>() {
                    @Override
                    public void onNext(List<MyArticle> list) {
                        if(currentState != null) {
                            currentState.setCurrentArticles(list);
                        } else {
                            currentState = new MainFragmentState(list);
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
    public int getTabCount() {
        if (currentState != null) {
            return currentState.getCurrentSortedArticles().keySet().size();
        }

        return 0;
    }

    @Override
    public void onClickBookmark(long articleId) {
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
                repoBookmark.insertArticle(article);
            } else {
                repoBookmark.deleteArticle(article);
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

    }

    /**
     * Выдать список статей согласно индексу таба (отдельная категория статей)
     */
    @Override
    public List<MyArticle> getTabArticles(int tabPosition) {

        if (currentState != null) {

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
        if (currentState != null) {
            return currentState.getCurrentArticles();
        }
        return new ArrayList<>();
    }

    @Override
    public List<MyArticle> getArticle(int listPosition) {
        return null;
    }
}
