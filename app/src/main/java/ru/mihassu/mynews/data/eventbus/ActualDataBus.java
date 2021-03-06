package ru.mihassu.mynews.data.eventbus;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.DataSnapshot;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ActualDataBus {

    String NO_HIGHLIGHT = "";

    // Точка подключения клиентов к отформатированным данным
    Observable<DataSnapshot> connectToActualData();

    Observable<List<MyArticle>> connectToBookmarkData();

    // Клиентский запрос на обновления
    void updateActualData();

    // Клиент просит объявить по "шине" результаты поиска
    void broadcastSearchResult(DataSnapshot dataSnapshot);
}
