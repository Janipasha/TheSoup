package in.thesoup.thesoup.GSONclasses.FeedGSON;

/**
 * Created by Jani on 06-04-2017.
 */

public class StoryData {

    String story_id;
    String story_name;
    String image_url;
    String story_modified;
    String cat_id;
    String hex_colour;
    String cat_name;
    String substory_id;
    String substory_name;
    String substory_created;
    String article_image_url;
    String article_title;
    String num_articles;
    String latest_substory;
    String active;
    String read;



    public String getStoryName() {

        return story_name;

    }

    public String getSubStoryName() {
        return substory_name;
    }

    public String getNumArticle() {
        return num_articles;
    }

    public String getTime() {
        return latest_substory;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getArticleImageUrl(){
        return article_image_url;
    }

    public String getStoryId(){
        return story_id;
    }

    public String getFollowStatus(){
        return active;
    }

    public void changeFollowStatus(String status){active = status;}

    public String getCategoryName(){return cat_name;}

    public String getCategoryColour(){return hex_colour;}

    public String getCategoryID(){return cat_id;}

    public String getReadStatus(){
        return read;
    }



}


