package ru.mihassu.mynews.ui;

import android.content.Intent;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.mihassu.mynews.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animatable2 animLeft = ((Animatable2) ((ImageView) findViewById(R.id.animLeft)).getDrawable());
        Animatable2 animRight = ((Animatable2) ((ImageView) findViewById(R.id.animRight)).getDrawable());
        Animatable2 animText = ((Animatable2) ((ImageView) findViewById(R.id.animText)).getDrawable());

        animText.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        animLeft.start();
        animRight.start();
        animText.start();
    }
}
