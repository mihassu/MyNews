package ru.mihassu.mynews.data;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ActualDataBus {
    // Точка подключения клиентов
    Observable<List<MyArticle>> connectToActualData();

    // Клиентский запрос на обновления
    void updateActualData();

    // Клиент просит объявить по "шине" результаты поиска
    void broadcastSearchResult(List<MyArticle> searchResult);
}
