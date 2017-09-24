package in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain;

import java.util.List;


/**
 * Created by Jani on 21-06-2017.
 */

public class DataStoriesMain {

    List<StoryDataMain> stories;
    String result_count;

    public  List<StoryDataMain> getStoryDataList(){
        return stories;
    }

    public String getfollowedStoryiesCount(){
        return result_count;
    }

}
