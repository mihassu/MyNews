package ru.mihassu.mynews.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import io.reactivex.Observable;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.ArticlePresenter;
import ru.mihassu.mynews.ui.Fragments.ViewHolderAnimated;
import ru.mihassu.mynews.ui.Fragments.ViewHolderBase;
import ru.mihassu.mynews.ui.Fragments.ViewHolderStatic;

public class MainAdapter extends RecyclerView.Adapter<ViewHolderBase> implements IMainAdapter {

    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top};

    private Consumer<String> clickHandler;
    private Observable<Integer> scrollEventsObs;
    private ArticlePresenter articlePresenter;

    public MainAdapter(Observable<Integer> scrollEventsObs,
                       Consumer<String> clickHandler,
                       ArticlePresenter articlePresenter) {
        this.clickHandler = clickHandler;
        this.scrollEventsObs = scrollEventsObs;
        this.articlePresenter = articlePresenter;

        articlePresenter.setAdapter(this);
    }

    @NonNull
    @Override
    public ViewHolderBase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Рандомно выбрать макет для элемента списка
//        int i = new Random().nextInt(itemLayouts.length);
//        View v = inflater.inflate(itemLayouts[i], parent, false);
        int i = viewType;
        View v = inflater.inflate(itemLayouts[i], parent, false);

        // Вызов браузера при клике на элементе списка
        v.setOnClickListener(view ->
                clickHandler.accept(view.getTag().toString())
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
    public int getItemViewType(int position) {
        return Math.abs(articlePresenter
                .getArticles()
                .get(position)
                .title.hashCode() % itemLayouts.length);
    }

    @Override
    public int getItemCount() {
        return articlePresenter.getArticles().size();
    }

    // articlePresenter.getArticles().get() wrapper
    private MyArticle getArticle(int position) {
        return articlePresenter.getArticles().get(position);
    }

    @Override
    public void onItemUpdated(int position) {
        notifyItemChanged(position);
    }
}