package ru.mihassu.mynews.ui.Fragments;

import android.view.View;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.news.VerticalMotionListener;

import static ru.mihassu.mynews.Utils.logIt;

/**
 * ViewHolder для элемента списка с анимированным контентом
 */
public class ViewHolderAnimated extends ViewHolderBase {

    private VerticalMotionListener motionListener;

    public ViewHolderAnimated(@NonNull View itemView, Observable<Integer> scrollObservable) {
        super(itemView);

        motionListener = itemView.findViewById(R.id.motionLayout);

        Disposable d = scrollObservable.subscribe(unused -> {
            motionListener.setDebugFlag(true);
            motionListener.onOffsetChanged(itemView.getY());
        });
    }

    public void bind(MyArticle item) {
        super.bind(item);
        motionListener.onUpdateMaxOffset(motionListener.minStartOffset);
    }
}