package ru.mihassu.mynews.ui.Fragments.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import io.reactivex.Observable;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;
import ru.mihassu.mynews.ui.main.ItemUpdateListener;
import ru.mihassu.mynews.ui.viewholder.ViewHolderAnimated;
import ru.mihassu.mynews.ui.viewholder.ViewHolderBase;
import ru.mihassu.mynews.ui.viewholder.ViewHolderStatic;

public class MainAdapter
        extends ListAdapter<MyArticle, ViewHolderBase>
        implements ItemUpdateListener {

    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top};

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
                       ArticlePresenter articlePresenter,
                       int tabPosition) {
        super(DiffCallback);
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
                articlePresenter.onClickArticle(view.getTag().toString())
        );

        ViewHolderBase holder =
                itemLayouts[i] == R.layout.item_article_image_top ?
                        new ViewHolderAnimated(v, articlePresenter, this, scrollEventsObs) :
                        new ViewHolderStatic(v, articlePresenter, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBase holder, int position) {
        holder.bind(getArticle(position));
    }

    @Override
    public int getItemCount() {
        return articlePresenter.getTabArticles(tabPosition).size();
    }

    // Wrapper articlePresenter.getTabArticles().get()
    private MyArticle getArticle(int position) {
        return articlePresenter.getTabArticles(tabPosition).get(position);
    }

    /**
     * ItemUpdateListener::onItemUpdated
     */
    @Override
    public void onItemUpdated(int position) {
        notifyItemChanged(position);
    }
}