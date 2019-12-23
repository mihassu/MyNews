package ru.mihassu.mynews.domain.channel;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

import static ru.mihassu.mynews.Utils.logIt;

public class ChannelParser {

    private static final String TAG_RSS = "rss";
    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_ITEM = "item";

    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LINK = "link";
    private static final String TAG_DATE = "pubDate";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_ENCLOSURE = "enclosure";
    private static final String TAG_CATEGORY = "category";

    private static final int TAG_ID_TITLE = 1;
    private static final int TAG_ID_DESCRIPTION = 2;
    private static final int TAG_ID_LINK = 3;
    private static final int TAG_ID_DATE = 4;
    private static final int TAG_ID_AUTHOR = 5;
    private static final int TAG_ID_ENCLOSURE = 6;
    private static final int TAG_ID_CATEGORY = 7;

    private static final String ATTR_TYPE = "type";
    private static final String ATTR_URL = "url";

    private Classifier classifier;

    // Не использовать namespace
    private static final String ns = null;

    public ChannelParser(Classifier classifier) {
        this.classifier = classifier;
    }

    public List<MyArticle> parse(InputStream in)
            throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);

            // Перейти на следующий тег, проверить, что это "<rss>"
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, ns, TAG_RSS);

            // Перейти на следующий тег, проверить, что это "<channel>"
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, ns, TAG_CHANNEL);

            return readChannel(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Распрарсить XML-контент
     */
    private List<MyArticle> readChannel(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        List<MyArticle> items = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals(TAG_ITEM)) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    /**
     * Парсим содержимое элемента <item>
     */
    private MyArticle readItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, TAG_ITEM);

        String title = null;
        String link = null;
        String description = null;
        String author = null;
        String image = null;
        ArticleCategory category = null;
        long pubDate = 0;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            switch (parser.getName()) {
                case TAG_TITLE:
                    title = readTag(parser, TAG_ID_TITLE);
                    break;
                case TAG_DESCRIPTION:
                    description = readTag(parser, TAG_ID_DESCRIPTION);
                    break;
                case TAG_LINK:
                    String tempLink = readTag(parser, TAG_ID_LINK);
                    if (tempLink != null) {
                        link = tempLink;
                    }
                    break;
                case TAG_DATE:
                    pubDate = parseDate(readTag(parser, TAG_ID_DATE));
                    break;
                case TAG_AUTHOR:
                    author = readTag(parser, TAG_ID_AUTHOR);
                    break;
                case TAG_ENCLOSURE:
                    image = readTag(parser, TAG_ID_ENCLOSURE);
                    break;
                case TAG_CATEGORY:
                    category = classifier.classify(readTag(parser, TAG_ID_CATEGORY));
                    if(category != ArticleCategory.NEWS) {
                        logIt("Parsed category: " + category);
                    }
                    break;
                default:
                    skip(parser);
            }
        }
        return new MyArticle(title, description, link, pubDate, author, image, category);
    }

    /**
     * Прочитать значение из тега
     */
    private String readTag(XmlPullParser parser, int tagType)
            throws IOException, XmlPullParserException {

        switch (tagType) {
            case TAG_ID_TITLE:
                return readBasicTag(parser, TAG_TITLE);
            case TAG_ID_DESCRIPTION:
                return readBasicTag(parser, TAG_DESCRIPTION);
            case TAG_ID_LINK:
                return readBasicTag(parser, TAG_LINK);
            case TAG_ID_DATE:
                return readBasicTag(parser, TAG_DATE);
            case TAG_ID_AUTHOR:
                return readBasicTag(parser, TAG_AUTHOR);
            case TAG_ID_ENCLOSURE:
                return readEnclosureLink(parser);
            case TAG_ID_CATEGORY:
                return readBasicTag(parser, TAG_CATEGORY);
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    /**
     * Прочитать значение тега, у которого нет вложенных тегов
     */
    private String readBasicTag(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);

        return result;
    }

    /**
     * Получить ссылку на картинку, которая упакована в тег <enclosure>
     */
    private String readEnclosureLink(XmlPullParser parser)
            throws IOException, XmlPullParserException {

        String url = null;
        parser.require(XmlPullParser.START_TAG, ns, TAG_ENCLOSURE);

        String type = parser.getAttributeValue(null, ATTR_TYPE);

        if (type != null && type.toLowerCase().contains("image")) {
            url = parser.getAttributeValue(null, ATTR_URL);
        }
        while (true) {
            if (parser.nextTag() == XmlPullParser.END_TAG) break;
        }
        return url;
    }

    /**
     * Прочитать содержимое тега как текст
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * Пропустить неинтересные теги.
     * @depth регулирует соответствие открывающих-закрывающих тегов у вложенных элементов
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     * Отпарсить время из строки вида "Tue, 17 Dec 2019 00:00:00 +0300"
     * в миллисекунды
     */
    private long parseDate(String date) {
        long result = System.currentTimeMillis();

        try {

            result = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
                    .parse(date)
                    .getTime();
        }
        catch (NullPointerException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}