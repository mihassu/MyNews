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
import io.reactivex.observers.DisposableObserver;
import ru.mihassu.mynews.data.ActualDataBus;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.MainFragmentPresenter;
import ru.mihassu.mynews.ui.Fragments.main.MainFragmentState;
import ru.mihassu.mynews.ui.main.BookmarkChangeListener;

import static ru.mihassu.mynews.Utils.logIt;

public class MainFragmentPresenterImp implements MainFragmentPresenter {

    private ActualDataBus dataBus;

    private MutableLiveData<MainFragmentState> liveData = new MutableLiveData<>();
    private Disposable searchDisposable;

    public MainFragmentPresenterImp(ActualDataBus dataBus) {
        this.dataBus = dataBus;
        subscribeToDataSources();
    }

    private void subscribeToDataSources() {
        dataBus
                .connectToActualData()
                .subscribe(new DisposableObserver<List<MyArticle>>() {
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
        dataBus.updateActualData();
    }

    @Override
    public void onFragmentConnected(Observable<List<MyArticle>> searchObservable) {
        searchDisposable = searchObservable
                .subscribe(
                        list -> dataBus.broadcastSearchResult(list)
                );
    }

    @Override
    public void onFragmentDisconnected() {
        if (searchDisposable != null && !searchDisposable.isDisposed()) {
            searchDisposable.dispose();
        }
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
}
