package ru.mihassu.mynews.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.data.network.RegnumApi;
import ru.mihassu.mynews.data.network.RetrofitInit;
import ru.mihassu.mynews.data.repository.ArticleRepositoryRegnum;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ArticleRepository;
import ru.mihassu.mynews.ui.main.MainViewModel;
import ru.mihassu.mynews.ui.main.MainViewModelFactory;
import ru.mihassu.mynews.ui.news.NewsViewPagerAdapter;

public class MainFragment extends Fragment implements Observer {

    private MainViewModel viewModel;

    private NewsViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    private ProgressBar progressBar;
    private MainFragmentState currentState;

    // 1.
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = viewFragment.findViewById(R.id.load_progress);
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
        loadChannels();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onChanged(Object obj) {
        currentState = (MainFragmentState) obj;
        progressBar.setVisibility(View.INVISIBLE);
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

        inflater.inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String text = s.toLowerCase();
                List<MyArticle> searchedList = new ArrayList<>();
                List<MyArticle> currentList = currentState.getCurrentArticles();
                for (MyArticle article: currentList) {
                    String title = article.title.toLowerCase();
                    if (title.contains(text)) {
                        searchedList.add(article);
                    }
                }
                if (searchedList.size() > 0) {
                    currentState.setCurrentArticles(searchedList);
                    viewPager.setCurrentItem(0);
                    viewPagerAdapter.updateContent(currentState.getCurrentEnumMap());
                } else {
                    Toast.makeText(getActivity(), "Не найдено", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
}
