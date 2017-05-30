package in.thesoup.thesoup.GSONclasses.SinglestoryGSON;

import java.util.List;

/**
 * Created by Jani on 11-04-2017.
 */

public class Substoryjsondata {

    String story_id;
    List<Substories> substories;
    String story_name;
    String image_url;
    String active;

    public String getfollowStatus() {
        return active;
    }

    public List<Substories> getSubstories() {
        return substories;
    }

    public String getStoryName() {
        return story_name;
    }

    public String getStoryId(){
        return story_id;
    }

    public void updateStoryId(String storyId) {
        story_id = storyId;
    }

    public void updateFollowStatus(String follow) {
        active = follow;

    }

    public void updateStoryName(String storyname) {
        story_name = storyname;
    }

    public void updateSubstoriesList(List<Substories> substorieslist) {
        for (int i = 0; i < substorieslist.size(); i++) {
            substories.add(substorieslist.get(i));
        }
    }
}
