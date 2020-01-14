package ru.mihassu.mynews.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.ViewHolderStatic;

import static ru.mihassu.mynews.Utils.logIt;

public class MainAdapter extends RecyclerView.Adapter<ViewHolderStatic> {

    private static final int ITEM_WITH_PIC = 1;
    private static final int ITEM_NO_PIC = 2;
    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top_cl};

    private List<MyArticle> dataList = new ArrayList<>();
    private Consumer<String> clickHandler;

    public MainAdapter(Consumer<String> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setDataList(List<MyArticle> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderStatic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Рандомно выбрать макет для элемента списка
        int i = new Random().nextInt(itemLayouts.length);
        View v = inflater.inflate(itemLayouts[i], parent, false);

        v.setOnClickListener(view -> {
            clickHandler.accept(view.getTag().toString());
        });

        return new ViewHolderStatic(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderStatic holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).image != null ? ITEM_WITH_PIC : ITEM_NO_PIC;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}