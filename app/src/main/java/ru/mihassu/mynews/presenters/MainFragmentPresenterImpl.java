package ru.mihassu.mynews.presenters;

import ru.mihassu.mynews.data.repository.RoomRepo;

public class MainFragmentPresenterImpl implements MainFragmentPresenter {

    private RoomRepo roomRepo;

    public MainFragmentPresenterImpl(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }
}
