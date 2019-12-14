package ru.mihassu.mynews.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


    private List<MyArticle> dataList = new ArrayList<>();


    public void setDataList(List<MyArticle> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_article, parent, false);
        return new MainAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        TextView itemContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemTitle = itemView.findViewById(R.id.item_title);
            this.itemContent = itemView.findViewById(R.id.item_content);
        }

        public void bind(MyArticle item) {
            itemTitle.setText(item.getTitle());
            itemContent.setText(item.getContent());
        }
    }
}
