package ru.mihassu.mynews.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;

import static ru.mihassu.mynews.Utils.logIt;

public class ViewHolderBase extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int contentThreshold = 120;

    private long articleId;
    private View itemView;
    private TextView itemTitle;
    private TextView itemContent;
    private TextView itemSourceStamp;
    private ImageView itemPreview;
    private ImageView itemFavicon;
    private ImageView itemBookmark;
    private ArticlePresenter presenter;

    ViewHolderBase(@NonNull View itemView, @NotNull ArticlePresenter presenter) {
        super(itemView);
        this.itemView = itemView;
        this.presenter = presenter;

        this.itemTitle = itemView.findViewById(R.id.item_title);
        this.itemContent = itemView.findViewById(R.id.item_content);
        this.itemPreview = itemView.findViewById(R.id.item_preview);
        this.itemFavicon = itemView.findViewById(R.id.favicon);
        this.itemSourceStamp = itemView.findViewById(R.id.source_stamp);
        this.itemBookmark = itemView.findViewById(R.id.flag_bookmark);
        itemBookmark.setOnClickListener(this);
    }

    @Override
    public void onClick(View bookmarkImageView) {
        presenter.onClickBookmark(this.articleId);
    }

    public void bind(MyArticle article) {
        articleId = article.id;
        // Ссылку на контент статьи сохр в теге элемента списка
        itemView.setTag(article.link);
        // Заголовок статьи
        itemTitle.setText(article.title.trim());
        // Основной текст
        itemContentSetText(article);
        // Основная картинка
        itemPreviewSetImage(article);
        // Футер
        itemFooterShow(article);
        // Картинка Bookmark
        itemBookmarkSetImage(article);
    }

    // Показать картинку
    private void itemPreviewSetImage(MyArticle item) {
        if (item.image != null) {
            Picasso
                    .get()
                    .load(item.image)
                    .into(itemPreview);
        } else {
            Picasso
                    .get()
                    .load(R.drawable.news_logo)
                    .into(itemPreview);
        }
    }

    // Основной текст
    private void itemContentSetText(MyArticle item) {
        String content = item.description.trim();
        if (content.length() > contentThreshold) {
            content = content.substring(0, contentThreshold) + "...";
        }
        itemContent.setText(content);
    }

    // Показать favicon.ico и дату новости
    private void itemFooterShow(MyArticle item) {
        try {
            String host = new URL(item.link).getHost();
            String iconRequest =
                    "https://www.google.com/s2/favicons?domain_url=http%3A%2F%2F" + host + "%2F";
            Picasso
                    .get()
                    .load(iconRequest)
                    .into(itemFavicon);

            String sourceStamp =
                    String.format(
                            "%s %s %s",
                            host,
                            Character.toString((char) 183),
                            articleTime(item.pubDate));

            itemSourceStamp.setText(sourceStamp);

        } catch (MalformedURLException e) {
            logIt("favicon load error");
            e.printStackTrace();
        }
    }

    // Картинка Bookmark
    private void itemBookmarkSetImage(MyArticle article) {
        itemBookmark.setImageResource(
                article.isMarked ?
                        R.drawable.ic_selected :
                        R.drawable.ic_unselected);
    }

    /**
     * Время выхода новости
     */
    private String articleTime(long time) {
        long current = System.currentTimeMillis();
        long pastHours = (current - time) / (60 * 60 * 1000);

        if (pastHours > 24) {
            return new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(time);
        } else if (pastHours >= 1) {

            String h = pastHours == 1 ? "hour" : "hours";

            return String.format(Locale.getDefault(), "%d %s ago",
                    pastHours, h);
        } else {
            return String.format(Locale.getDefault(), "%s",
                    new SimpleDateFormat("H:mm", Locale.getDefault()).format(time));
        }
    }
}