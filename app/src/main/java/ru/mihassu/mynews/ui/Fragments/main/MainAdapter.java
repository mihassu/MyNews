package ru.mihassu.mynews.ui.fragments.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;
import ru.mihassu.mynews.ui.viewholder.ItemUpdateListener;
import ru.mihassu.mynews.ui.viewholder.ViewHolderAnimated;
import ru.mihassu.mynews.ui.viewholder.ViewHolderBase;
import ru.mihassu.mynews.ui.viewholder.ViewHolderStatic;

public class MainAdapter extends RecyclerView.Adapter<ViewHolderBase>
        implements ItemUpdateListener {

    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top};

    private Observable<Integer> scrollEventsObs;
    private ArticlePresenter articlePresenter;
    private int tabPosition;

    public MainAdapter(Observable<Integer> scrollEventsObs,
                       ArticlePresenter articlePresenter,
                       int tabPosition) {
        this.scrollEventsObs = scrollEventsObs;
        this.articlePresenter = articlePresenter;
        this.tabPosition = tabPosition;

        this.articlePresenter.bindBookmarkChangeListener(this);
    }

    /**
     * itemType - индекс в массиве layout'ов, т.е. возвр индекс ID макета
     */
    @Override
    public int getItemViewType(int position) {
        return Math.abs(articlePresenter
                .getTabArticles(tabPosition)
                .get(position)
                .title.hashCode() % itemLayouts.length);
    }

    /**
     * Длина списка статей в категории (в tabPosition)
     */
    @Override
    public int getItemCount() {
        return articlePresenter.getTabArticles(tabPosition).size();
    }

    @NonNull
    @Override
    public ViewHolderBase onCreateViewHolder(@NonNull ViewGroup parent, int viewTypeAkaLayoutIndex) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(itemLayouts[viewTypeAkaLayoutIndex], parent, false);

        // Вызов браузера при клике на элементе списка. View элемента списка хранит
        // в теге url-статьи. Значение тега устанавливается в ViewHolder'е.
        v.setOnClickListener(view ->
                articlePresenter.onClickArticle(view.getTag().toString())
        );

        ViewHolderBase holder =
                itemLayouts[viewTypeAkaLayoutIndex] == R.layout.item_article_image_top ?
                        new ViewHolderAnimated(v, articlePresenter, this, scrollEventsObs) :
                        new ViewHolderStatic(v, articlePresenter, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBase holder, int position) {
        MyArticle a = getArticle(position);
        holder.bind(getArticle(position));
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