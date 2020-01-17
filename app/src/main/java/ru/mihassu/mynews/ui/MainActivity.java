package ru.mihassu.mynews.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.di.components.ui.DaggerMainActivityComponent;
import ru.mihassu.mynews.di.modules.ui.MainActivityModule;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences preferences;

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainActivityComponent
                .builder()
                .activityModule(new MainActivityModule())
                .addDependency(((App) getApplication()).getAppComponent())
                .build()
                .inject(this);

        setAppTheme();
        setContentView(R.layout.activity_main);
        showBottomNavigationMenu();
        initToolbar();
        initNavigationDrawer();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_main,
                R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();

    }

    private void setAppTheme() {
        boolean userTheme =
                preferences.getBoolean(
                        getString(R.string.pref_key_dark_theme),
                        false);

        if (userTheme) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemePurple);
        }
    }

    private void showBottomNavigationMenu() {
        View bottomNavigationMenu = findViewById(R.id.bottom_navigation);

        boolean navigationMenuEnabled =
                preferences.getBoolean(
                        getString(R.string.pref_key_show_bottom_navigation),
                        false);

        if (navigationMenuEnabled) {
            bottomNavigationMenu.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationMenu.setVisibility(View.GONE);
        }
    }
}