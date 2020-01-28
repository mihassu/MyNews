package ru.mihassu.mynews.data.eventbus;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.DataSnapshort;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ActualDataBus {
    // Точка подключения клиентов к отформатированным данным
    Observable<DataSnapshort> connectToActualData();

    Observable<List<MyArticle>> connectToBookmarkData();

    // Клиентский запрос на обновления
    void updateActualData();

    // Клиент просит объявить по "шине" результаты поиска
    void broadcastSearchResult(List<MyArticle> searchResult);
}
