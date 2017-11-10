package in.thesoup.thesoupstoriesnews.gsonclasses.Follow;

import java.util.List;

/**
 * Created by Jani on 11-04-2017.
 */

public class followunfollow {

    List<followunfollowdata> data;
    String error;

    public class followunfollowdata{
     String story_id;
        String user_id;

        public String getStoryid(){
            return story_id;
        }

        public String getUserid(){
            return user_id;
        }
    }
}


