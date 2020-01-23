package ru.mihassu.mynews.ui.Fragments.bookmark;

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
import ru.mihassu.mynews.presenters.i.BookmarkFragmentPresenter;
import ru.mihassu.mynews.ui.main.BookmarkChangeListener;
import ru.mihassu.mynews.ui.viewholder.ViewHolderAnimated;
import ru.mihassu.mynews.ui.viewholder.ViewHolderBase;
import ru.mihassu.mynews.ui.viewholder.ViewHolderStatic;
import ru.mihassu.mynews.ui.web.BrowserLauncher;

public class BookmarkAdapter extends ListAdapter<MyArticle, ViewHolderBase> {

    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right};

    private BookmarkFragmentPresenter presenter;

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

    public BookmarkAdapter(BookmarkFragmentPresenter presenter) {
        super(DiffCallback);
        this.presenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        return Math.abs(presenter
                .getArticle(position)
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
                presenter.onClickArticle(view.getTag().toString())
        );

        ViewHolderBase holder = new ViewHolderStatic(v, presenter);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBase holder, int position) {
        holder.bind(getArticle(position));
    }

    @Override
    public int getItemCount() {
        return presenter.getArticles().size();
    }

    // Wrapper presenter.getArticle(position)
    private MyArticle getArticle(int position) {
        return presenter.getArticle(position);
    }
}