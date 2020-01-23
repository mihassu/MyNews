package ru.mihassu.mynews.presenters;

import ru.mihassu.mynews.data.ActualDataBus;

abstract class BasePresenter {
    ActualDataBus dataBus;

    BasePresenter(ActualDataBus dataBus) {
        this.dataBus = dataBus;
    }

    // Подписаться на данные из коллектора и список bookmark'ов из базы
    abstract void subscribeToDataSources();
}
