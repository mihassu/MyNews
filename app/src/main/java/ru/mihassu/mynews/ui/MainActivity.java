package ru.mihassu.mynews.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import ru.mihassu.mynews.R;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationDrawer();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_main, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void initView() {
        ProgressBar progressBar = findViewById(R.id.main_progressbar);
//        ImageView toolbarImage = findViewById(R.id.main_toolbar_image);
//        Picasso
//                .get()
//                .load("https://regnum.ru/assets/img/logo_base.png")
//                .into(toolbarImage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();

    }

    private void setAppTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean userTheme = preferences.getBoolean("dark_theme", false);
        if (userTheme) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemePurple);
        }
    }

}