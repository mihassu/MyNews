package ru.mihassu.mynews.ui.custom;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import ru.mihassu.mynews.R;

public class CustomSnackbar extends BaseTransientBottomBar<CustomSnackbar> {

    private CustomSnackBarView contentView;
    private ImageView ivSb;
    private ImageView ivPoints;
    private TextView tvSb;

    public CustomSnackbar(@NonNull ViewGroup parent,
                          @NonNull CustomSnackBarView contentView) {
        super(parent, contentView, contentView);

        this.contentView = contentView;
        this.ivSb = contentView.findViewById(R.id.iv_sb);
        this.tvSb = contentView.findViewById(R.id.tv_sb);
        this.ivPoints = contentView.findViewById(R.id.iv_points);

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

    @NonNull
    @Override
    public CustomSnackbar setDuration(int duration) {
        return super.setDuration(duration);
    }

    @NonNull
    public CustomSnackbar setText(@StringRes int resId) {
        tvSb.setText(getContext().getString(resId));
        return this;
    }

    @NonNull
    public CustomSnackbar setIcon(@DrawableRes int resId) {
        ivSb.setImageDrawable(getContext().getDrawable(resId));
        ivSb.setVisibility(View.VISIBLE);
        return this;
    }

    @NonNull
    public CustomSnackbar setBackground(@DrawableRes int resId) {
        contentView.setBackground(getContext().getDrawable(resId));
        return this;
    }

    @NonNull
    public CustomSnackbar setOnClickHandler(Runnable runnable) {
        contentView.setOnClickListener(v -> {

            AnimatedVectorDrawableCompat animatedProgressBar =
                    AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_moving_points_sb);

            ivPoints.setImageDrawable(animatedProgressBar);

            if (animatedProgressBar != null) {
                animatedProgressBar.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        ivPoints.post(animatedProgressBar::start);
                    }
                });
                animatedProgressBar.start();
            }

            ivPoints.setVisibility(View.VISIBLE);
            ivSb.setVisibility(View.INVISIBLE);
            tvSb.setVisibility(View.INVISIBLE);

            runnable.run();
        });
        return this;
    }

    @NotNull
    @Contract("_ -> new")
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