package in.thesoupstoriesnews.thesoup.GSONclasses.FollowingGSON;

import java.util.List;

import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.UnRead;

/**
 * Created by Jani on 21-06-2017.
 */

public class DataStoriesFollowing {

    List<StoryDataFollowing> stories;
    String unseen;

    public  List<StoryDataFollowing> getStoryDataList(){
        return stories;
    }

    public String getUnseen(){
        return unseen;
    }
}
