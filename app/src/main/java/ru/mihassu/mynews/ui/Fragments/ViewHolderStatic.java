package ru.mihassu.mynews.ui.Fragments;

import android.view.View;

import androidx.annotation.NonNull;

import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.ArticlePresenter;

/**
 * ViewHolder для элемента списка со статическим контентом
 */
public class ViewHolderStatic extends ViewHolderBase {

    public ViewHolderStatic(@NonNull View itemView, ArticlePresenter presenter) {
        super(itemView, presenter);
    }

    public void bind(MyArticle item) {
        super.bind(item);
    }
}