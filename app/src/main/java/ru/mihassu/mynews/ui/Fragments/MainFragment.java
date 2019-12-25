package ru.mihassu.mynews.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

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
import ru.mihassu.mynews.ui.news.NewsPageAdapter;

public class MainFragment extends Fragment {
    private MainViewModel viewModel;
    private View fragmentView;

    private NewsPageAdapter viewPagerAdapter;
    private ViewPager2 viewPager;


    // 1.
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        initViewPager();
        return fragmentView;
    }

    // 2. Tabs
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.news_tabs);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                //Установка заголовка таба
                tab.setText(view.getContext().getString(ArticleCategory.values()[position].getTextId())))
                .attach();
    }

    // 3.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        initViewModel();
        loadChannels();
    }

    // ViewPager
    private void initViewPager() {
        viewPagerAdapter = new NewsPageAdapter();
        viewPager = fragmentView.findViewById(R.id.news_viewpager);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void initViewModel() {
        RegnumApi api = RetrofitInit.newApiInstance();
        ArticleRepository repository = new ArticleRepositoryRegnum(api);
        viewModel = ViewModelProviders.of(this, new MainViewModelFactory(repository))
                .get(MainViewModel.class);
    }

    // Запускаем процесс получения данных
    private void loadChannels() {
        App app = (App) Objects.requireNonNull(getActivity()).getApplication();

        app
                .getCollector()
                .collectChannels()
                .observe(this,
                        articleList -> {

                            // Порядок ключей будет совпадать с порядке элементов в ArticleCategory
                            EnumMap<ArticleCategory, List<MyArticle>> enumMap = new EnumMap<>(ArticleCategory.class);

                            for (ArticleCategory c : EnumSet.allOf(ArticleCategory.class)) {
                                enumMap.put(c, new ArrayList<>());
                            }

                            for (MyArticle article : articleList) {
                                Objects.requireNonNull(enumMap.get(article.category)).add(article);
                            }

                            drawList(enumMap);
                        }
                );
    }

    private void drawList(EnumMap<ArticleCategory, List<MyArticle>> enumMap) {
        viewPagerAdapter.setClassifiedNews(enumMap);
    }

}
