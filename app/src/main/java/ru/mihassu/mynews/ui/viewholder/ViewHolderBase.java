package ru.mihassu.mynews.ui.viewholder;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;
import ru.mihassu.mynews.ui.main.ItemUpdateListener;

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
    private ItemUpdateListener itemUpdateListener;
    private boolean isMarked;

    private static final int[] STATE_SET_ON =
            {R.attr.state_on};
    private static final int[] STATE_SET_OFF =
            {R.attr.state_off};

    private static ArrayList<int[]> bookmarkStates = new ArrayList<>(
            Arrays.asList(STATE_SET_ON, STATE_SET_OFF)
    );

    ViewHolderBase(@NonNull View itemView,
                   @NotNull ArticlePresenter presenter,
                   @NotNull ItemUpdateListener itemUpdateListener) {
        super(itemView);
        this.itemView = itemView;
        this.presenter = presenter;

        this.itemTitle = itemView.findViewById(R.id.item_title);
        this.itemContent = itemView.findViewById(R.id.item_content);
        this.itemPreview = itemView.findViewById(R.id.item_preview);
        this.itemFavicon = itemView.findViewById(R.id.favicon);
        this.itemSourceStamp = itemView.findViewById(R.id.source_stamp);
        this.itemUpdateListener = itemUpdateListener;

        this.itemBookmark = itemView.findViewById(R.id.flag_bookmark);
        itemBookmark.setOnClickListener(this);
    }

    /**
     * При клике на bookmark оповестить презентера, для изменения состояний в базе и адаптере.
     */
    @Override
    public void onClick(View bookmarkImageView) {
        presenter.onClickBookmark(this.articleId);
        this.isMarked = !this.isMarked;

        itemBookmark.setImageState(
                isMarked ?
                        bookmarkStates.get(0) :
                        bookmarkStates.get(1),
                false);
    }

    public void bind(MyArticle article) {

        this.isMarked = article.isMarked;

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
            logIt("VHB: favicon load error");
            e.printStackTrace();
        }
    }

    // Картинка Bookmark
    private void itemBookmarkSetImage(MyArticle article) {
        itemBookmark.setImageState(
                article.isMarked ?
                        bookmarkStates.get(0) :
                        bookmarkStates.get(1),
                false);
    }

    /**
     * Время выхода новости
     */
    private String articleTime(long time) {
        long current = System.currentTimeMillis();
        long pastHours = (current - time) / (60 * 60 * 1000);

        Context context = itemView.getContext();

        if (pastHours > 24) {
            return new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(time);
        } else if (pastHours >= 1) {

            String h = hoursToString(pastHours, context);

            return String.format(Locale.getDefault(), "%d %s %s",
                    pastHours, h, context.getString(R.string.time_ago));
        } else {
            return String.format(Locale.getDefault(), "%s",
                    new SimpleDateFormat("H:mm", Locale.getDefault()).format(time));
        }
    }

    private String hoursToString(long hours, Context context) {

        if(hours >= 10 && hours < 21) {
            return context.getString(R.string.time_hours_5_0);
        }

        String szHours = Long.toString(hours);
        int n =  Integer.valueOf(Character.toString(szHours.charAt(szHours.length() - 1)));

        int stringId = R.string.time_hours_5_0;
        if(n >= 2 && n < 5) {
            stringId = R.string.time_hours_2_4;
        } else if(n == 1) {
            stringId = R.string.time_hour;
        }
        return context.getString(stringId);
    }


}