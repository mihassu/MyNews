package ru.mihassu.mynews.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.data.network.RegnumApi;
import ru.mihassu.mynews.data.network.RetrofitInit;
import ru.mihassu.mynews.data.repository.ArticleRepositoryRegnum;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ArticleRepository;
import ru.mihassu.mynews.ui.main.MainAdapter;
import ru.mihassu.mynews.ui.main.MainViewModel;
import ru.mihassu.mynews.ui.main.MainViewModelFactory;
import ru.mihassu.mynews.ui.web.ArticleActivity;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

import static ru.mihassu.mynews.Utils.logIt;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private MainViewModel viewModel;
    private ProgressBar progressBar;

    private CustomTabHelper customTabHelper = new CustomTabHelper();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        initFab();
        initViewModel();
        initRecyclerView();

        loadChannels(disposable);

//        viewModel.articleLiveData.observe(this, data -> {
//            adapter.setDataList(data);
//            progressBar.setVisibility(View.GONE);
//        });
//
//        viewModel.loadArticles();
//        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    private void drawList(List<MyArticle> listItems) {

        adapter.setDataList(listItems);
        System.out.println("APP_TAG items received " + listItems.size());
    }

    private void initView() {
        progressBar = findViewById(R.id.main_progressbar);
        ImageView toolbarImage = findViewById(R.id.main_toolbar_image);
        Picasso
                .get()
                .load("https://regnum.ru/assets/img/logo_base.png")
                .into(toolbarImage);
    }

    private void initRecyclerView() {
        adapter = new MainAdapter(this::startContentViewer);
        RecyclerView rv = findViewById(R.id.news_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initViewModel() {
        RegnumApi api = RetrofitInit.newApiInstance();
        ArticleRepository repository = new ArticleRepositoryRegnum(api);
        viewModel = ViewModelProviders.of(this, new MainViewModelFactory(repository))
                .get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Запускаем процесс получения данных
     */
    private void loadChannels(CompositeDisposable disposable) {
        App app = (App) getApplication();
        disposable.add(
                app
                        .getCollector()
                        .collectChannels()
                        .map(list -> {
                            List<MyArticle> sortedList = new ArrayList<>();
                            sortedList.addAll(list);
                            Collections.sort(sortedList);
                            return sortedList;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::drawList)
        );
    }

    private void startContentViewer(String link) {

        int requestCode = 100;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.white));
        builder.addDefaultShareMenuItem();
        builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out);
        builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out);
        builder.setShowTitle(true);

        CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();

        Intent intent = anotherCustomTab.intent;
        intent.setData(Uri.parse(link));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addMenuItem("Our custom menu", pendingIntent);

        CustomTabsIntent customTabsIntent = builder.build();

        String packageName = customTabHelper.getPackageNameToUse(this, link);

        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(this, Uri.parse(link));
        } else {
            Intent intentOpenUri = new Intent(this, ArticleActivity.class);
            intentOpenUri.putExtra(getString(R.string.article_url_key), link);
            startActivity(intentOpenUri);
        }
    }
}