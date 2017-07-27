package in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON;

import java.util.List;

/**
 * Created by Jani on 21-06-2017.
 */

public class DataStories {

    List<StoryData> stories;
    UnRead num_unread;

    public  List<StoryData> getStoryDataList(){
        return stories;
    }

    public UnRead getUnRead(){
        return num_unread;

    }
}
