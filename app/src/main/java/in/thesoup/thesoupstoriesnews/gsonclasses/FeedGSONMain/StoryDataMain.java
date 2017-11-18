package in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain;

import java.util.List;

/**
 * Created by Jani on 06-04-2017.
 */

public class StoryDataMain {

    String story_id;
    String story_name;
    String cat_id;
    String hex_colour;
    String cat_name;
    String substory_ids;
    String substory_names;
    String substory_dates;
    String image_urls;
    String story_date;
    String read;
    String num_substories;
    String unread_count;
   List< SubstoriesMain> substories;
    String follow_status;
    String substory_read;


    public String getStoryIdMain(){
        return story_id;
    }

    public String getStoryNameMain() {
        return story_name;
    }

    public String getCategoryId(){
        return  cat_id;
    }

    public String getHexColor(){
        return hex_colour;
    }

    public  String getCategoryName(){
        return cat_name;
    }

    public  String getSubstoryId(){
        return substory_ids;
    }

    public  String getSubstoryName(){
        return  substory_names;
    }

    public String getSubstoryDates(){
        return substory_dates;
    }

    public String getImageUrls(){
        return image_urls;
    }

    public String getStoryDate(){
        return story_date;
    }

    public String getReadStatus(){
        return read;
    }

    public String getNumberSubstories(){
        return num_substories;
    }

    public List<SubstoriesMain> getSubstories(){
        return substories;
    }

    public String getFollowstatus(){return follow_status;}

    public String getUnreadCount(){
        return unread_count;
    }


    public void changeFollowStatus(String followstatus) {
        follow_status = followstatus;
    }
}


