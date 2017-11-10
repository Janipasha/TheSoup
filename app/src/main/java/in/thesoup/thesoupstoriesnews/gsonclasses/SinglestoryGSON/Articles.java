package in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON;

import java.io.Serializable;

/**
 * Created by Jani on 11-04-2017.
 */

public class Articles implements Serializable {

    String article_id;
    String title;
    String description;
    String url;
    String image_url;
    String pub_date;
    String author;
    String source_name;
    String source_thumb;


    public String getArticleTitle() {
        return title;
    }
    public String getImageUrl(){return image_url;}
    public String getSourceName(){return  source_name;}
    public String getUrl(){return url;}
    public String getSourceLogo(){
        return source_thumb;
    }
    public String getTime(){return pub_date;};
    public String getArticleId(){
        return article_id;
    }

}
