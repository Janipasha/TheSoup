package in.thesoupstoriesnews.thesoup.GSONclasses.FollowingGSON;

/**
 * Created by Jani on 06-09-2017.
 */

public class StoryDataFollowing {

    String story_id;
    String story_name;
    String num_unseen;
    String substory_names;
    String latest_update;
    String follow_date;
    String cat_id;
    String hex_colour;
    String cat_name;


    public String getStoryId(){
        return  story_id;
    }

    public String getStoryName(){
        return story_name;
    }

    public String getNumUnseen(){
        return num_unseen;
    }

    public String getSubstoryNames(){
        return  substory_names;
    }

    public String getLatestUpdate(){
        return  latest_update;
    }

    public String getFollowDate(){
        return  follow_date;
    }

    public String getCategoryName(){
        return  cat_name;
    }

    public  String getHexColor(){
        return hex_colour;
    }
}
