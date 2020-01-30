package ru.mihassu.mynews.ui.custom;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import ru.mihassu.mynews.R;

public class CustomSnackbar extends BaseTransientBottomBar<CustomSnackbar> {

    public CustomSnackbar(@NonNull ViewGroup parent,
                          @NonNull CustomSnackBarView contentView) {
        super(parent, contentView, contentView);

        // Позиционировать содержимое Snackbar'а (кастомную CustomSnackBarView) по центру
        // родительского контейнера (SnackbarLayout/FrameLayout)
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentView.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        contentView.setLayoutParams(params);

        // В контейнере SnackbarLayout установить прозрачный фон и отступы
        View snackbarLayout = getView();
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        snackbarLayout.setPadding(0, 0, 0, 0);
    }

    public CustomSnackbar(@NonNull ViewGroup parent,
                          @NonNull CustomSnackBarView contentView,
                          @NonNull String text,
                          int iconId) {
        super(parent, contentView, contentView);

        // Позиционировать содержимое Snackbar'а (кастомную CustomSnackBarView) по центру
        // родительского контейнера (SnackbarLayout/FrameLayout)
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentView.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        contentView.setLayoutParams(params);

        ImageView iv = contentView.findViewById(R.id.iv_sb);
        TextView tv = contentView.findViewById(R.id.tv_sb);

        tv.setText(text);
        iv.setImageResource(iconId);

        // В контейнере SnackbarLayout установить прозрачный фон и отступы
        View snackbarLayout = getView();
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        snackbarLayout.setPadding(0, 0, 0, 0);
    }

    public static CustomSnackbar make(@NonNull View view, int iconId, String text) {

        ViewGroup parent = findSuitableParent((ViewGroup) view);

        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found");
        }

        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        CustomSnackBarView customView =
                (CustomSnackBarView) inflater
                        .inflate(R.layout.layout_snackbar_content_view, parent, false);

        return new CustomSnackbar(parent, customView, text, iconId);

    }


    public static CustomSnackbar make(@NonNull View view) {
        ViewGroup parent = findSuitableParent((ViewGroup) view);

        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found");
        }

        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        CustomSnackBarView customView =
                (CustomSnackBarView) inflater
                        .inflate(R.layout.layout_snackbar_content_view, parent, false);

        return new CustomSnackbar(parent, customView);
    }

    private static ViewGroup findSuitableParent(ViewGroup view) {
        ViewGroup fallback = null;

        do {
            if (view instanceof CoordinatorLayout) {
                return view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return view;
                } else {
                    fallback = view;
                }
            }

            if (view != null) {
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (ViewGroup) parent : null;
            }

        } while (view != null);

        return fallback;
    }
}

