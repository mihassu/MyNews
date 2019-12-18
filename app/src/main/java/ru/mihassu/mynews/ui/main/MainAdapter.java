package ru.mihassu.mynews.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static final int ITEM_WITH_PIC = 1;
    private static final int ITEM_NO_PIC = 2;
    private static int[] itemLayouts = {
            R.layout.item_article_image_left,
            R.layout.item_article_image_right,
            R.layout.item_article_image_top};

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
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Рандомно выбрать макет для элемента списка
        int i = new Random().nextInt(itemLayouts.length);
        View v = inflater.inflate(itemLayouts[i], parent, false);

        v.setOnClickListener(view -> {
            clickHandler.accept(view.getTag().toString());
        });

        return new MainAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView itemTitle;
        TextView itemContent;
        TextView itemLink;
        ImageView itemPreview;
        int maxSize = 120;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            this.itemView = itemView;
            this.itemTitle = itemView.findViewById(R.id.item_title);
            this.itemContent = itemView.findViewById(R.id.item_content);
            this.itemPreview = itemView.findViewById(R.id.item_preview);
        }

        public void bind(MyArticle item) {

            // Ссылку на контент статьи сохр в теге элемента списка
            itemView.setTag(item.link);

            itemTitle.setText(item.title);

            String content = item.description;

            if(content.length() > maxSize ){
                content = content.substring(0, maxSize) + "...";
            }

            itemContent.setText(content);

            if (item.image != null) {
                Picasso
                        .get()
                        .load(item.image)
                        .into(itemPreview);
            } else {
                Picasso
                        .get()
                        .load(R.drawable.ic_news_logo)
                        .into(itemPreview);
            }
        }
    }
}