package in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome;

import java.io.Serializable;

/**
 * Created by Jani on 11-11-2017.
 */

public class ArticlesMainHome implements Serializable {
    String article_id;
    String title;
    String description;
    String url;
    String image_url;
    String pub_date;
    String author;
    String source_name;
    String source_thumb;


    public  String getArticleId(){
        return  article_id;
    }

    public  String getTitle(){
        return title;
    }

    public String getDescription(){
        return  description;
    }

    public String getUrl(){
        return url;
    }

    public String getImageUrl(){
        return image_url;
    }

    public String getPubDate(){
        return pub_date;
    }


    public String getAuthor(){
        return author;
    }

    public String getsourceName(){
        return source_name;
    }

    public String getSourceThumb(){
        return source_thumb;
    }
}
