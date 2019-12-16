package ru.mihassu.mynews.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.repository.ArticleRepository;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private MainViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        initFab();
        initViewModel();
        initRecyclerView();

        viewModel.articleLiveData.observe(this, data -> {
            adapter.setDataList(data);
            progressBar.setVisibility(View.GONE);
        });

        viewModel.loadArticles();
        progressBar.setVisibility(View.VISIBLE);

    }

    private void initView() {
        progressBar = findViewById(R.id.main_progressbar);
        ImageView toolbarImage = findViewById(R.id.main_toolbar_image);
        Picasso.with(this).load("https://regnum.ru/assets/img/logo_base.png").into(toolbarImage);
    }

    private void initRecyclerView() {
        adapter = new MainAdapter();
        RecyclerView rv = findViewById(R.id.news_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(layoutManager);
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

        ArticleRepository repository = ((App) getApplication()).getAppComponent().getRepository();
//        RegnumApi api = RetrofitInit.newApiInstance();
//        ArticleRepository repository = new ArticleRepositoryRegnum(api);
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
}
