package ru.mihassu.mynews.ui.Fragments.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.di.modules.ui.BookmarkFragmentModule;
import ru.mihassu.mynews.presenters.i.BookmarkFragmentPresenter;

import static ru.mihassu.mynews.Utils.logIt;

public class BookmarksFragment extends Fragment implements Observer {

    @Inject
    Context context;

    @Inject
    BookmarkAdapter adapter;

    @Inject
    BookmarkFragmentPresenter bookmarkPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        App
                .get()
                .getAppComponent()
                .plusBookmarkFragmentComponent(new BookmarkFragmentModule(this))
                .inject(this);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        bookmarkPresenter.onFragmentConnected();

//        setHasOptionsMenu(true);
        return viewFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.bookmarks_list);
        initRecyclerView(rv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookmarkPresenter.onFragmentDisconnected();

    }

    @Override
    public void onChanged(Object o) {
        logIt("BF::onChanged");
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView(RecyclerView rv) {
        bookmarkPresenter.subscribe().observe(this, this);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setHasFixedSize(false);
        rv.setAdapter(adapter);
    }
}
