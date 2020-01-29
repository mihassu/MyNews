package ru.mihassu.mynews.domain.search;

import androidx.appcompat.widget.SearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchObservable {

    public static Observable<String> fromView(SearchView searchView) {

        return Observable.create((ObservableOnSubscribe<String>) emitter ->

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        emitter.onNext(s.toLowerCase());
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        emitter.onNext(s.toLowerCase());
                        return true;
                    }
                }))
                .debounce(1000, TimeUnit.MILLISECONDS)
                .switchMap(text -> Observable.fromCallable(() -> text.toLowerCase().trim()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
