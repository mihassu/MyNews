package ru.mihassu.mynews.ui.news;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.ArticlePresenter;
import ru.mihassu.mynews.ui.Fragments.BrowserLauncher;
import ru.mihassu.mynews.ui.Fragments.UpdateAgent;
import ru.mihassu.mynews.ui.main.MainAdapter;
import ru.mihassu.mynews.ui.web.ArticleActivity;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

import static ru.mihassu.mynews.Utils.logIt;

public class NewsViewPagerAdapter
        extends RecyclerView.Adapter<NewsViewPagerAdapter.NewsViewHolder> {

    // Презентер для элементов ViewPager'а и элементов списка статей
    private ArticlePresenter articlePresenter;

    // Helper для работы с Chrome CustomTabs
    private CustomTabHelper customTabHelper = new CustomTabHelper();

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
     * Делаем ItemViewType равным его позиции в списке страниц ViewPager'а и будем использовать в
     * onCreateViewHolder чтобы сообщить ему номер таба. Это связывает таб с соотв категорией
     * статей (и списком статей).
     */
    @Override
    public int getItemViewType(int position) {
        return position;
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
        holder.bind(articlePresenter.getTabArticles(tabPosition));
    }

    @Override
    public int getItemCount() {
        return articlePresenter.getTabCount();
    }

    /**
     * Вызывается из MainFragment при обновлении новостей
     */
    public void updateContent() {
        isUpdateInProgress = false;
        notifyDataSetChanged();
    }

    /**
     * Holder отдельной ViewGroup внутри ViewPager2
     */
    class NewsViewHolder extends RecyclerView.ViewHolder implements BrowserLauncher  {

        private SwipeRefreshLayout swipeRefreshLayout;
        private BehaviorSubject<Integer> scrollEventsRelay;
        private ArticlePresenter articlePresenter;
        private int tabPosition;

        /**
         * Адаптер для списка новостей нужно создавать в этом месте, чтобы он потом
         * не пересоздавался при каждом обновлении данных.
         */
        NewsViewHolder(@NonNull View itemView, @NonNull ArticlePresenter presenter, int tabPosition) {
            super(itemView);

            this.articlePresenter = presenter;
            this.scrollEventsRelay = BehaviorSubject.create();
            this.tabPosition = tabPosition;

            RecyclerView rv = itemView.findViewById(R.id.news_recyclerview);
            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (dy != 0) {
                        scrollEventsRelay.onNext(dy);
                    }
                }
            });

            logIt("NewsViewHolder::tabPosition=" + tabPosition);

            MainAdapter adapter = new MainAdapter(
                    scrollEventsRelay.hide(),
                    this,
                    articlePresenter,
                    tabPosition);
            rv.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rv.setHasFixedSize(false);
            rv.setAdapter(adapter);

            initSwipeRefreshLayout();
        }

        // Передать данные в презентер
        void bind(List<MyArticle> list) {
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

        // Отобразить новость в Chrome CustomTabs


        @Override
        public void showInBrowser(String url) {

            Context context = itemView.getContext();
            int requestCode = 100;

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

            builder.setToolbarColor(ContextCompat.getColor(context, android.R.color.white));
            builder.addDefaultShareMenuItem();
            builder.setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out);
            builder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out);
            builder.setShowTitle(true);

            CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();

            Intent intent = anotherCustomTab.intent;
            intent.setData(Uri.parse(url));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addMenuItem("Our custom menu", pendingIntent);

            CustomTabsIntent customTabsIntent = builder.build();

            String packageName = customTabHelper.getPackageNameToUse(context, url);

            if (packageName != null) {
                customTabsIntent.intent.setPackage(packageName);
                customTabsIntent.launchUrl(context, Uri.parse(url));
            } else {
                Intent intentOpenUri = new Intent(context, ArticleActivity.class);
                intentOpenUri.putExtra(itemView.getResources().getString(R.string.article_url_key), url);
                context.startActivity(intentOpenUri);
            }
        }
    }
}
