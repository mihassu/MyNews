package ru.mihassu.mynews.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ArticleRepository;

public class MainViewModel extends ViewModel {

    private ArticleRepository repository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<MyArticle>> articleLiveData = new MutableLiveData<>();


    public MainViewModel(ArticleRepository repository) {
        this.repository = repository;
    }

    public void loadArticles() {
        compositeDisposable.add(repository.getArticles()
                .subscribe(result -> articleLiveData.setValue(result),
                        throwable -> System.out.println(throwable)
                ));

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
