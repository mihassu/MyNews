package ru.mihassu.mynews.di.modules.ui;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.data.repository.RoomRepo;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.presenters.MainFragmentPresenter;
import ru.mihassu.mynews.presenters.MainFragmentPresenterImpl;

@Module
public class MainFragmentModule {

    @Provides
    @FragmentScope
    public MainFragmentPresenter getPresenter(RoomRepo roomRepo) {
        return new MainFragmentPresenterImpl(roomRepo);
    }

}
