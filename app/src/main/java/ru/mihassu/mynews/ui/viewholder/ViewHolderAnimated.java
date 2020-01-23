package ru.mihassu.mynews.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;
import ru.mihassu.mynews.ui.custom.VerticalMotionListener;

/**
 * ViewHolder для элемента списка с анимированным контентом
 */
public class ViewHolderAnimated extends ViewHolderBase {

    private VerticalMotionListener motionListener;

    public ViewHolderAnimated(@NonNull View itemView, ArticlePresenter presenter, Observable<Integer> scrollObservable) {
        super(itemView, presenter);

        motionListener = itemView.findViewById(R.id.motionLayout);

        Disposable d = scrollObservable.subscribe(unused -> {
            motionListener.setDebugFlag(false);
            motionListener.onOffsetChanged(itemView.getY());
        });
    }

    public void bind(MyArticle item) {
        super.bind(item);
        motionListener.onUpdateMaxOffset(motionListener.minStartOffset);
    }
}