package ru.mihassu.mynews.di.components.ui;

import dagger.Subcomponent;
import ru.mihassu.mynews.di.modules.ui.BookmarkFragmentModule;
import ru.mihassu.mynews.di.qualifiers.BookmarkFragmentScope;
import ru.mihassu.mynews.ui.fragments.bookmark.BookmarksFragment;

@BookmarkFragmentScope
@Subcomponent(modules = {BookmarkFragmentModule.class})
public interface BookmarkFragmentComponent {
    void inject(BookmarksFragment fragment);
}
