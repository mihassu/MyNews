package ru.mihassu.mynews.ui.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.data.network.RegnumApi;
import ru.mihassu.mynews.data.network.RetrofitInit;
import ru.mihassu.mynews.data.repository.ArticleRepositoryRegnum;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ArticleRepository;
import ru.mihassu.mynews.domain.search.SearchObservable;
import ru.mihassu.mynews.ui.main.MainViewModel;
import ru.mihassu.mynews.ui.main.MainViewModelFactory;
import ru.mihassu.mynews.ui.news.NewsViewPagerAdapter;

public class MainFragment extends Fragment implements Observer {

    private MainViewModel viewModel;

    private NewsViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    private MainFragmentState currentState;
    private AnimatedVectorDrawableCompat animatedProgressBar;
    private ImageView progressBarImage;
    private TextView progressBarText;
    private ConstraintLayout progressBarContainer;
    private Observable<String> searchObservable;
    private CompositeDisposable disposables = new CompositeDisposable();

    // 1.
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_main, container, false);

        progressBarContainer = viewFragment.findViewById(R.id.pb_container);
        progressBarImage = progressBarContainer.findViewById(R.id.iv_moving_points);
        progressBarText = progressBarContainer.findViewById(R.id.tv_loading);

        initViewPager(viewFragment);
        setHasOptionsMenu(true);
        return viewFragment;
    }

    // 2. Tabs
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initTabLayout(view);
    }

    // 3.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initProgressBar(getContext());
        loadChannels();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onChanged(Object obj) {
        currentState = (MainFragmentState) obj;

        // Убрать ProgressBar
        progressBarContainer.setVisibility(View.INVISIBLE);
        if (animatedProgressBar != null) {
            animatedProgressBar.stop();
        }

        // Показать новости
        viewPagerAdapter.updateContent(currentState.getCurrentEnumMap());
    }

    // Init ViewPager
    private void initViewPager(View fragmentView) {
        viewPagerAdapter = new NewsViewPagerAdapter(this::updateAgentImpl);
        viewPager = fragmentView.findViewById(R.id.news_viewpager);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void initTabLayout(@NonNull View fragment) {
        TabLayout tabLayout = fragment.findViewById(R.id.news_tabs);

        TabLayoutMediator mediator = new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {
                    tab.setText(fragment
                            .getContext()
                            .getString(currentState.getCurrentCategories()[position].getTextId()));
//                                .getString(ArticleCategory.values()[position].getTextId()));
                }
        );
        mediator.attach();
    }

    /**
     * Запустить кастомный ProgressBar
     */
    private void initProgressBar(Context context) {
        animatedProgressBar =
                AnimatedVectorDrawableCompat.create(context, R.drawable.avd_moving_points);
        progressBarImage.setImageDrawable(animatedProgressBar);

        if (animatedProgressBar != null) {
            animatedProgressBar.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    progressBarImage.post(animatedProgressBar::start);
                }
            });
            animatedProgressBar.start();
        }
    }

    private void initViewModel() {
        RegnumApi api = RetrofitInit.newApiInstance();
        ArticleRepository repository = new ArticleRepositoryRegnum(api);
        viewModel = ViewModelProviders.of(this, new MainViewModelFactory(repository))
                .get(MainViewModel.class);
    }

    // UpdateAgent::update()
    private void updateAgentImpl() {
        App app = (App) Objects.requireNonNull(getActivity()).getApplication();
        app.getCollector().updateChannels();
    }

    /**
     * Запускаем процесс получения данных
     * На выходе получаем списки статей упорядоченные по категориям в HashMap'е
     */
    @SuppressWarnings("unchecked")
    private void loadChannels() {
        App app = (App) Objects.requireNonNull(getActivity()).getApplication();

        app
                .getCollector()
                .collectChannels()
                .observe(this,
                        this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchObservable = SearchObservable.fromView(searchView);
        disposables.add(searchObservable.subscribe(text -> {
            List<MyArticle> searchedList = new ArrayList<>();
                List<MyArticle> currentList = currentState.getArticles();
                for (MyArticle article : currentList) {
                    String title = article.title.toLowerCase();
                    if (title.contains(text)) {
                        searchedList.add(article);
                    }
                }
                if (searchedList.size() > 0) {
                    currentState.setCurrentArticles(searchedList);
                    viewPager.setCurrentItem(0);
                    viewPagerAdapter.updateContent(currentState.getCurrentEnumMap());
                    viewPagerAdapter.setSearchText(text);
                }

                },
                throwable -> {}));


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                String text = s.toLowerCase();
//                List<MyArticle> searchedList = new ArrayList<>();
//                List<MyArticle> currentList = currentState.getCurrentArticles();
//                for (MyArticle article : currentList) {
//                    String title = article.title.toLowerCase();
//                    if (title.contains(text)) {
//                        searchedList.add(article);
//                    }
//                }
//                if (searchedList.size() > 0) {
//                    currentState.setCurrentArticles(searchedList);
//                    viewPager.setCurrentItem(0);
//                    viewPagerAdapter.updateContent(currentState.getCurrentEnumMap());
//                } else {
//                    Toast.makeText(getActivity(), getString(R.string.not_found), Toast.LENGTH_SHORT).show();
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
