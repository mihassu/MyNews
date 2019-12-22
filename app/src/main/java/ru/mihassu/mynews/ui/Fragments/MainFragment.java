package ru.mihassu.mynews.ui.Fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
import ru.mihassu.mynews.ui.news.NewsPageAdapter;
import ru.mihassu.mynews.ui.web.ArticleActivity;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

public class MainFragment extends Fragment {
    private MainAdapter adapter;
    private MainViewModel viewModel;
    private View view;

    private NewsPageAdapter viewPagerAdapter;
    private ViewPager2 viewPager;

//    private CustomTabHelper customTabHelper = new CustomTabHelper();
    private CompositeDisposable disposable = new CompositeDisposable();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initViewPager();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
//        initRecyclerView();
        loadChannels(disposable);
    }

    //Tabs
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.news_tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText("Newss"))
        .attach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    private void drawList(List<MyArticle> listItems) {

//        adapter.setDataList(listItems);
        List<List<MyArticle>> newsList = new ArrayList<>();
        newsList.add(listItems);
        newsList.add(listItems);
        viewPagerAdapter.setDataList(newsList);
        System.out.println("APP_TAG items received " + listItems.size());
    }

//    private void initRecyclerView() {
//        adapter = new MainAdapter(this::startContentViewer);
//        RecyclerView rv = view.findViewById(R.id.news_recyclerview);
//        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        rv.setAdapter(adapter);
//    }

    private void initViewModel() {
        RegnumApi api = RetrofitInit.newApiInstance();
        ArticleRepository repository = new ArticleRepositoryRegnum(api);
        viewModel = ViewModelProviders.of(this, new MainViewModelFactory(repository))
                .get(MainViewModel.class);
    }

    //ViewPager
    private void initViewPager() {
        viewPagerAdapter = new NewsPageAdapter();
        viewPager = view.findViewById(R.id.news_viewpager);
        viewPager.setAdapter(viewPagerAdapter);
    }


    /**
     * Запускаем процесс получения данных
     */
    private void loadChannels(CompositeDisposable disposable) {
        App app = (App) Objects.requireNonNull(getActivity()).getApplication();
        disposable.add(
                app
                        .getCollector()
                        .collectChannels()
                        .map(list -> {
                            List<MyArticle> sortedList = new ArrayList<>(list);
                            Collections.sort(sortedList);
                            return sortedList;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::drawList)
        );
    }

//    private void startContentViewer(String link) {
//
//        int requestCode = 100;
//
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//
//        builder.setToolbarColor(ContextCompat.getColor(getContext(), android.R.color.white));
//        builder.addDefaultShareMenuItem();
//        builder.setStartAnimations(getContext(), android.R.anim.fade_in, android.R.anim.fade_out);
//        builder.setExitAnimations(getContext(), android.R.anim.fade_in, android.R.anim.fade_out);
//        builder.setShowTitle(true);
//
//        CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();
//
//        Intent intent = anotherCustomTab.intent;
//        intent.setData(Uri.parse(link));
//        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.addMenuItem("Our custom menu", pendingIntent);
//
//        CustomTabsIntent customTabsIntent = builder.build();
//
//        String packageName = customTabHelper.getPackageNameToUse(getContext(), link);
//
//        if (packageName != null) {
//            customTabsIntent.intent.setPackage(packageName);
//            customTabsIntent.launchUrl(getContext(), Uri.parse(link));
//        } else {
//            Intent intentOpenUri = new Intent(getContext(), ArticleActivity.class);
//            intentOpenUri.putExtra(getString(R.string.article_url_key), link);
//            startActivity(intentOpenUri);
//        }
//    }
}
