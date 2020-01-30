package ru.mihassu.mynews.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.ContentViewCallback;

import ru.mihassu.mynews.R;

public class CustomSnackBarView extends ConstraintLayout implements ContentViewCallback {

    public CustomSnackBarView(Context context) {
        super(context);
    }

    public CustomSnackBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSnackBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
        View.inflate(context, R.layout.layout_snackbar_content_view, this);
    }

    @Override
    public void animateContentIn(int delay, int duration) {
    }

    @Override
    public void animateContentOut(int delay, int duration) {
    }
}
