package ru.mihassu.mynews.presenters;

import ru.mihassu.mynews.data.repository.RoomRepoBookmark;

public class MainFragmentPresenterImpl implements MainFragmentPresenter {

    private RoomRepoBookmark roomRepoBookmark;

    public MainFragmentPresenterImpl(RoomRepoBookmark roomRepoBookmark) {
        this.roomRepoBookmark = roomRepoBookmark;
    }
}
