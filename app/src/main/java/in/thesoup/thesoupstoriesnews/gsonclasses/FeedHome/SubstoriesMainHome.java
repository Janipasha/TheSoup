package in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome;

import java.util.List;

import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.ArticlesMain;

/**
 * Created by Jani on 11-11-2017.
 */

public class SubstoriesMainHome {

    String substory_id;
    String substory_name;
    String update_number;
    String top_article_image;
    String top_article_date;
    String substory_read;
    List<ArticlesMainHome> articles;



    public String getSubStoryName() {
        return substory_name;
    }

    public String getSubstory_id(){return substory_id;}

    public String getUpdateNumber(){return update_number;}

    public String getTopArticleImage(){return top_article_image;}

    public String getTopArticleDate(){return  top_article_date;}

    public String getReadStatus(){
        return substory_read;
    }

    public  List<ArticlesMainHome> getArticlesMain(){return  articles;}
}


