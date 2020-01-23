package ru.mihassu.mynews.ui.Fragments.bookmark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.BookmarkFragmentPresenter;
import ru.mihassu.mynews.ui.main.ItemUpdateListener;
import ru.mihassu.mynews.ui.viewholder.ViewHolderBase;
import ru.mihassu.mynews.ui.viewholder.ViewHolderStatic;

public class BookmarkAdapter extends ListAdapter<MyArticle, ViewHolderBase> implements ItemUpdateListener {

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

        ViewHolderBase holder = new ViewHolderStatic(v, presenter, this);
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

    /**
     * ItemUpdateListener::onItemUpdated
     */
    @Override
    public void onItemUpdated(int position) {
        notifyItemChanged(position);
    }

    // Wrapper presenter.getArticle(position)
    private MyArticle getArticle(int position) {
        return presenter.getArticle(position);
    }
}