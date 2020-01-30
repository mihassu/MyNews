package ru.mihassu.mynews.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import ru.mihassu.mynews.R;

public class CustomSnackBarView extends ConstraintLayout {
    public CustomSnackBarView(Context context) {
        super(context);
    }

    public CustomSnackBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSnackBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);

        View.inflate(context, R.layout.layout_snackbar, this);
    }

    public CustomSnackBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, 0);
    }
}
