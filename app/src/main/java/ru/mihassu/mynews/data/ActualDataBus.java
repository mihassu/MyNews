package ru.mihassu.mynews.data;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ActualDataBus {
    Observable<List<MyArticle>> connectToActualData();
    void updateActualData();
}
