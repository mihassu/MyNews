package ru.mihassu.mynews.ui.Fragments.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;

import static ru.mihassu.mynews.Utils.logIt;

public class NewsViewPagerAdapter
        extends RecyclerView.Adapter<NewsViewPagerAdapter.NewsViewHolder> {

    // Презентер для элементов ViewPager'а и элементов списка статей
    private ArticlePresenter articlePresenter;

    // Интерфейс для ручного обновления
    private UpdateAgent updateAgent;
    private boolean isUpdateInProgress;

    public NewsViewPagerAdapter(
            UpdateAgent updateAgent,
            ArticlePresenter articlePresenter) {

        this.articlePresenter = articlePresenter;
        this.updateAgent = updateAgent;
        this.isUpdateInProgress = true;
    }

    /**
     * Делаем ItemViewType равным его позиции в списке табов ViewPager'а и будем использовать в
     * onCreateViewHolder чтобы сообщить ему номер таба. Это связывает таб с соотв категорией
     * статей (и списком статей).
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Определяет количество табов по количеству категорий текущего набора данных
     */
    @Override
    public int getItemCount() {
        return articlePresenter.getTabCount();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTypeAkaTabPosition) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(v, articlePresenter, viewTypeAkaTabPosition);
    }

    // v 1.2
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int tabPosition) {
        holder.bind();
    }

    /**
     * Вызывается из MainFragment при обновлении новостей
     */
    public void updateContent() {
        logIt("NVPA::updateContent()");
        isUpdateInProgress = false;
        notifyDataSetChanged();
    }

    /**
     * Holder отдельной ViewGroup внутри ViewPager2
     */
    class NewsViewHolder extends RecyclerView.ViewHolder {

        private SwipeRefreshLayout swipeRefreshLayout;
        private BehaviorSubject<Integer> scrollEventsRelay;

        /**
         * Адаптер для списка новостей нужно создавать в этом месте, чтобы он потом
         * не пересоздавался при каждом обновлении данных.
         */
        NewsViewHolder(@NonNull View itemView,
                       @NonNull ArticlePresenter presenter,
                       int tabPosition) {
            super(itemView);
            this.scrollEventsRelay = BehaviorSubject.create();

            RecyclerView rv = itemView.findViewById(R.id.news_recyclerview);
            rv.addOnScrollListener(
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            if (dy != 0) {
                                scrollEventsRelay.onNext(dy);
                            }
                        }
                    });

            MainAdapter adapter = new MainAdapter(
                    scrollEventsRelay.hide(),
                    presenter,
                    tabPosition);
            rv.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rv.setHasFixedSize(false);
            rv.setAdapter(adapter);

            initSwipeRefreshLayout();
        }

        // Данные предоставляет презентер, поэтому тут нечего биндить.
        // Просто отключить progressbar обновления.
        void bind() {
            swipeRefreshLayout.setRefreshing(false);
        }

        // Настроить работу SwipeRefreshLayout
        private void initSwipeRefreshLayout() {
            swipeRefreshLayout = itemView.findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
            );

            // Запросить обновление при Swipe
            swipeRefreshLayout.setOnRefreshListener(() -> {

                        if (!isUpdateInProgress) {
                            isUpdateInProgress = true;
                            swipeRefreshLayout.setRefreshing(true);
                            updateAgent.launchUpdate();
                        }
                    }
            );

            swipeRefreshLayout.setRefreshing(isUpdateInProgress);
        }
    }
}
