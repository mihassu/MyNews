package ru.mihassu.mynews.ui.custom;

/**
 * https://medium.com/@fabionegri/make-snackbar-great-again-51edf7c940d4
 */

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import ru.mihassu.mynews.R;

public class CustomSnackbar extends BaseTransientBottomBar<CustomSnackbar> {

    public CustomSnackbar(@NonNull ViewGroup parent,
                          @NonNull View content,
                          @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);

        View view = getView();
//
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)parent.getLayoutParams();
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//        parent.setLayoutParams(params);

        view.setForegroundGravity(Gravity.CENTER);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        view.setPadding(0, 0, 0, 0);


    }

    public static CustomSnackbar make(ViewGroup parent, int duration) {
        // inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_snackbar, parent, false);

        // create with custom view
        ContentViewCallback callback = new ContentViewCallback(view);
        CustomSnackbar customSnackbar = new CustomSnackbar(parent, view, callback);
        customSnackbar.setDuration(duration);

        return customSnackbar;
    }

    private static class ContentViewCallback
            implements BaseTransientBottomBar.ContentViewCallback {

        // view inflated from custom layout
        private View view;

        public ContentViewCallback(View view) {
            this.view = view;
        }

        @Override
        public void animateContentIn(int delay, int duration) {
            // TODO: handle enter animation
        }

        @Override
        public void animateContentOut(int delay, int duration) {
            // TODO: handle exit animation
        }
    }

}

