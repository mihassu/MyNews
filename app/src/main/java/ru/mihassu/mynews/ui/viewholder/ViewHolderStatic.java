package ru.mihassu.mynews.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;

/**
 * ViewHolder для элемента списка со статическим контентом
 */
public class ViewHolderStatic extends ViewHolderBase {

    public ViewHolderStatic(@NonNull View itemView,
                            @NotNull ArticlePresenter presenter,
                            @NotNull ItemUpdateListener itemUpdateListener) {
        super(itemView, presenter, itemUpdateListener);
    }

    public void bind(MyArticle item) {
        super.bind(item);
    }
}