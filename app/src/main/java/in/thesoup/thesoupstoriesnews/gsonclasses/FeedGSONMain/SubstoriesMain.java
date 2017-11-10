package in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain;

import java.util.List;

/**
 * Created by Jani on 05-09-2017.
 */

public class SubstoriesMain {

    String substory_id;
    String substory_name;
    String update_number;
    String top_article_image;
    String top_article_date;
    List<ArticlesMain> articles;



    public String getSubStoryName() {
        return substory_name;
    }

    public String getSubstory_id(){return substory_id;}

    public String getUpdateNumber(){return update_number;}

    public String getTopArticleImage(){return top_article_image;}

    public String getTopArticleDate(){return  top_article_date;}

    public  List<ArticlesMain> getArticlesMain(){return  articles;}
}
