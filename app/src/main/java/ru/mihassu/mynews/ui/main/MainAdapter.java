package ru.mihassu.mynews.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.ViewHolderAnimated;
import ru.mihassu.mynews.ui.Fragments.ViewHolderBase;
import ru.mihassu.mynews.ui.Fragments.ViewHolderStatic;

public class MainAdapter extends RecyclerView.Adapter<ViewHolderBase> {

    private static final int ITEM_WITH_PIC = 1;
    private static final int ITEM_NO_PIC = 2;
    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top_cl};

    private List<MyArticle> dataList = new ArrayList<>();
    private Consumer<String> clickHandler;
    private Observable<Integer> scrollEventsObs;
    private String searchText;

    public MainAdapter(Observable<Integer> scrollEventsObs, Consumer<String> clickHandler) {
        this.clickHandler = clickHandler;
        this.scrollEventsObs = scrollEventsObs;
    }

    public void setDataList(List<MyArticle> dataList, String searchText) {
        this.dataList = dataList;
        this.searchText = searchText;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderBase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Рандомно выбрать макет для элемента списка
//        int i = new Random().nextInt(itemLayouts.length);
//        View v = inflater.inflate(itemLayouts[i], parent, false);
        int i = viewType;
        View v = inflater.inflate(itemLayouts[i], parent, false);

        v.setOnClickListener(view -> {
            clickHandler.accept(view.getTag().toString());
        });

        ViewHolderBase holder = itemLayouts[i] == R.layout.item_article_image_top_cl ?
                new ViewHolderAnimated(v, scrollEventsObs) : new ViewHolderStatic(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBase holder, int position) {
        holder.setSearchText(searchText);
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return Math.abs(dataList.get(position).title.hashCode() % itemLayouts.length);
//        return dataList.get(position).image != null ? ITEM_WITH_PIC : ITEM_NO_PIC;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}