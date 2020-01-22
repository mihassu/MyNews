package ru.mihassu.mynews.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import io.reactivex.Observable;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.ArticlePresenter;
import ru.mihassu.mynews.ui.Fragments.BrowserLauncher;
import ru.mihassu.mynews.ui.Fragments.ViewHolderAnimated;
import ru.mihassu.mynews.ui.Fragments.ViewHolderBase;
import ru.mihassu.mynews.ui.Fragments.ViewHolderStatic;

public class MainAdapter
        extends ListAdapter<MyArticle, ViewHolderBase>
        implements BookmarkChangeListener {

    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top};

    private BrowserLauncher browserLauncher;
    private Observable<Integer> scrollEventsObs;
    private ArticlePresenter articlePresenter;
    private int tabPosition;

    private static DiffUtil.ItemCallback<MyArticle> DiffCallback = new DiffUtil.ItemCallback<MyArticle>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyArticle oldItem, @NonNull MyArticle newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyArticle oldItem, @NonNull MyArticle newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MainAdapter(Observable<Integer> scrollEventsObs,
                       BrowserLauncher browserLauncher,
                       ArticlePresenter articlePresenter,
                       int tabPosition) {
        super(DiffCallback);
        this.browserLauncher = browserLauncher;
        this.scrollEventsObs = scrollEventsObs;
        this.articlePresenter = articlePresenter;
        this.tabPosition = tabPosition;

        this.articlePresenter.bindBookmarkChangeListener(this);
    }

    @Override
    public int getItemViewType(int position) {
        return Math.abs(articlePresenter
                .getTabArticles(tabPosition)
                .get(position)
                .title.hashCode() % itemLayouts.length);
    }

    @NonNull
    @Override
    public ViewHolderBase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        int i = viewType;
        View v = inflater.inflate(itemLayouts[i], parent, false);

        // Вызов браузера при клике на элементе списка
        v.setOnClickListener(view ->
                browserLauncher.showInBrowser(view.getTag().toString())
        );

        ViewHolderBase holder =
                itemLayouts[i] == R.layout.item_article_image_top ?
                        new ViewHolderAnimated(v, articlePresenter, scrollEventsObs) :
                        new ViewHolderStatic(v, articlePresenter);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBase holder, int position) {
        holder.bind(getArticle(position));
    }

    @Override
    public int getItemCount() {
        return articlePresenter.getTabArticles(0).size();
    }

    // articlePresenter.getTabArticles().get() wrapper
    private MyArticle getArticle(int position) {
        return articlePresenter.getTabArticles(tabPosition).get(position);
    }

    /**
     * BookmarkChangeListener::onItemUpdated
     */
    @Override
    public void onItemUpdated(int position) {
        notifyItemChanged(position);
    }
}