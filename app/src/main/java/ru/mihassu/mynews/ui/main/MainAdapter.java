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
import java.util.function.Consumer;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


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
        View v;

        if(viewType == 0) {
            v = inflater.inflate(R.layout.item_article, parent, false);
        } else {
            v = inflater.inflate(R.layout.item_article_with_img, parent, false);
        }

        v.setOnClickListener( view -> {
            TextView tv = view.findViewById(R.id.item_link);
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
        if(dataList.get(position).image != null) {
            return 1;
        } else {
            return 0;
        }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemTitle = itemView.findViewById(R.id.item_title);
            this.itemContent = itemView.findViewById(R.id.item_content);
            this.itemLink = itemView.findViewById(R.id.item_link);
            this.itemPreview = itemView.findViewById(R.id.item_preview);
        }

        public void bind(MyArticle item) {

            // Ссылку на контент статьи сохр в теге элемента списка
            itemView.setTag(item.link);

            itemTitle.setText(item.title);
            itemContent.setText(item.description);

            if(itemLink != null) {
                itemLink.setText(item.link);
            }

            if(itemPreview != null && item.image != null) {
                Picasso
                        .get()
                        .load(item.image)
                        .into(itemPreview);
            }
        }
    }
}