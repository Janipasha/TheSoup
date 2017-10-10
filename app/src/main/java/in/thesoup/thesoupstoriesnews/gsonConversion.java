package in.thesoup.thesoupstoriesnews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thesoup.thesoupstoriesnews.Activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.Activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.Fragments.DiscoverFragmentMain;
import in.thesoup.thesoupstoriesnews.Fragments.FollowingFragment;
import in.thesoup.thesoupstoriesnews.Fragments.HomeFragment;
import in.thesoup.thesoupstoriesnews.GSONclasses.AuthVerify.Authverify;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSON.GetStoryFeed;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSONMain.GetStoryFeedMain;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.GSONclasses.FollowingGSON.GetStoryFeedFollowing;
import in.thesoup.thesoupstoriesnews.GSONclasses.FollowingGSON.StoryDataFollowing;
import in.thesoup.thesoupstoriesnews.GSONclasses.SinglestoryGSON.GetSingleStory;
import in.thesoup.thesoupstoriesnews.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoupstoriesnews.GSONclasses.SinglestoryGSON.Substoryjsondata;
import in.thesoup.thesoupstoriesnews.GSONclasses.filters.Filterdata;
import in.thesoup.thesoupstoriesnews.GSONclasses.filters.GetFilters;
import in.thesoup.thesoupstoriesnews.GSONclasses.filters1.FilterAPIJson;
import in.thesoup.thesoupstoriesnews.GSONclasses.filters1.Filters;

/**
 * Created by Jani on 07-04-2017.
 */

public class gsonConversion {


    private JSONObject mJsonObject,nJsonObject;
    private List<StoryData> mListFromJson;
    private List<StoryDataMain> nListFromJson,oListFromJson;
    private List<StoryDataFollowing> pListFromJson;
    private Substoryjsondata mSubstoryjsonData;
    private String StoryTitle ,mJsonString, followstatus;
    private List<Substories> mSubstories;
    private Filterdata filterdata;
    private String NetworkCallHome;


    public  void fillUiHome(JSONObject jsonObject,String networkCallHome1 ,Context context,int fragmenttag,String totalrefresh) {
        nListFromJson = new ArrayList<>();
        oListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        NetworkCallHome = networkCallHome1;


        Gson gson = new Gson();
        GetStoryFeedMain red = gson.fromJson(mJsonObject.toString(),GetStoryFeedMain.class);

        String followcount = "";
        String followUpdateCount= "";

        if(red.getDataStoriesMain().getfollowedStoryiesCount()!=null&&!red.getDataStoriesMain().getfollowedStoryiesCount().isEmpty()){
            followcount = red.getDataStoriesMain().getfollowedStoryiesCount();
        }

        if(red.getDataStoriesMain().getFollowedStoryUpdates()!=null&&!red.getDataStoriesMain().getFollowedStoryUpdates().isEmpty()){
            followUpdateCount = red.getDataStoriesMain().getFollowedStoryUpdates();
        }




        if(NetworkCallHome.equals("fromfollowed")){
            for (int i = 0; i < red.getDataStoriesMain().getStoryDataList().size(); i++) {


                    nListFromJson.add(red.getDataStoriesMain().getStoryDataList().get(i));

            }



                NavigationActivity activity = (NavigationActivity)context;

                if(fragmenttag==1&&totalrefresh.equals("0")) {

                    Fragment f = activity.getFragment(fragmenttag);

                    ((HomeFragment) f).startAdapterfirsttime(nListFromJson,oListFromJson,followcount,followUpdateCount);

                }else if(fragmenttag==1&&totalrefresh.equals("1")){
                    Fragment f = activity.getFragment(fragmenttag);
                    ((HomeFragment)f).startAdapterfirsttime(nListFromJson,oListFromJson,followcount,followUpdateCount);
                }

            } else  if(NetworkCallHome.equals("frommoreforyou")){

            for (int i = 0; i < red.getDataStoriesMain().getStoryDataList().size(); i++) {

                if (red.getDataStoriesMain().getStoryDataList().get(i).getCategoryName() != null && !red.getDataStoriesMain().getStoryDataList().get(i).getCategoryName().isEmpty()) {

                    oListFromJson.add(red.getDataStoriesMain().getStoryDataList().get(i));
                }
            }

                NavigationActivity activity = (NavigationActivity)context;

                if(fragmenttag==1&&totalrefresh.equals("0")) {
                    Fragment f = activity.getFragment(fragmenttag);
                    ((HomeFragment) f).startAdapterAfterPagination(oListFromJson);
                }else if(fragmenttag==1&&totalrefresh.equals("1")){
                    Fragment f = activity.getFragment(fragmenttag);
                    ((HomeFragment)f).startAdaptersecondcall(oListFromJson);
                }


            }
        }




    public  void fillUiMain(JSONObject jsonObject,Context context,int fragmenttag,String totalrefresh){
        nListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetStoryFeedMain red = gson.fromJson(jsonObject.toString(),GetStoryFeedMain.class);


        for (int i = 0; i < red.getDataStoriesMain().getStoryDataList().size(); i++) {

            if(red.getDataStoriesMain().getStoryDataList().get(i).getCategoryName()!=null&&!red.getDataStoriesMain().getStoryDataList().get(i).getCategoryName().isEmpty()){

                nListFromJson.add(red.getDataStoriesMain().getStoryDataList().get(i));
            }

        }

        NavigationActivity activity = (NavigationActivity)context;

        if(fragmenttag==0&&totalrefresh.equals("0")) {

            Fragment f = activity.getFragment(fragmenttag);

            ((DiscoverFragmentMain) f).startAdapter(nListFromJson);

        }else if(fragmenttag==0&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((DiscoverFragmentMain)f).startRefreshAdapter(nListFromJson);
        }



    }


    public void fillUI(JSONObject jsonObject,Context context,int fragmenttag,String totalrefresh) {

        mListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetStoryFeed red = gson.fromJson(jsonObject.toString(), GetStoryFeed.class);

      //  Log.d("readvalue",red.getDataStories().getUnRead().toString());

         String num_read= "";

        if(red.getDataStories().getUnRead().getUnReadValue()!=null&&!red.getDataStories().getUnRead().getUnReadValue().isEmpty()){

            num_read = red.getDataStories().getUnRead().getUnReadValue();
        }



        Log.d("Num_read",num_read);




        for (int i = 0; i < red.getDataStories().getStoryDataList().size(); i++) {

            if(red.getDataStories().getStoryDataList().get(i).getCategoryName()!=null&&!red.getDataStories().getStoryDataList().get(i).getCategoryName().isEmpty()){

                mListFromJson.add(red.getDataStories().getStoryDataList().get(i));
            }

        }


        //** changes for Navigation activity


/*        MainActivity activity = (MainActivity) context;*/

        NavigationActivity activity = (NavigationActivity)context;





       /* if(fragmenttag==1&&totalrefresh.equals("0")){
            Fragment f = activity.getFragment(fragmenttag);
            ((MyFeedFragment)f).startAdapter(mListFromJson,num_read);
        }else if(fragmenttag==1&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((MyFeedFragment)f).startRefreshAdapter(mListFromJson,num_read);
        }*/





    }

    public  void fillUiFollow(JSONObject jsonObject,Context context,int fragmenttag,String totalrefresh){
        pListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        String followunseen = "" ;

        Gson gson = new Gson();
        GetStoryFeedFollowing red = gson.fromJson(jsonObject.toString(),GetStoryFeedFollowing.class);

        if(red.getDataStories().getUnseen()!=null||!red.getDataStories().getUnseen().isEmpty()){
            followunseen = red.getDataStories().getUnseen();
        }


        for (int i = 0; i < red.getDataStories().getStoryDataList().size(); i++) {
                pListFromJson.add(red.getDataStories().getStoryDataList().get(i));

        }

        NavigationActivity activity = (NavigationActivity)context;
        if(fragmenttag==2&&totalrefresh.equals("0")) {
            Fragment f = activity.getFragment(fragmenttag);
            ((FollowingFragment) f).startAdapter(pListFromJson,followunseen);
        }else if(fragmenttag==2&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((FollowingFragment)f).startRefreshAdapter(pListFromJson,followunseen);
        }
    }



    public void fillStoryUI(JSONObject jsonObject, Context context,String totalRefresh){

        mSubstories = new ArrayList<>();

        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetSingleStory redstory = gson.fromJson(jsonObject.toString(),GetSingleStory.class);

        StoryTitle = redstory.getdata().getStoryName();
        String StoryId = redstory.getdata().getStoryId();

       for(int i=0;i<redstory.getdata().getSubstories().size();i++){

           mSubstories.add(redstory.getdata().getSubstories().get(i));
           Log.e("Substory heading",mSubstories.get(i).getSubstoryName());
        }

        Log.d("substories",mSubstories.toString());


        DetailsActivity activity = (DetailsActivity) context;

        if(totalRefresh.equals("0")) {

            activity.startAdapter(mSubstories, StoryTitle,StoryId);
        }else if (totalRefresh.equals("1")){
            activity.startRefreshAdapter(mSubstories,StoryId);
        }

        activity.sendFirebaseExpandEvent(mSubstories);

    }

    public void fillUINotification(JSONObject jsonObject,Context context,int fragmenttag,String totalrefresh) {

        mListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetStoryFeed red = gson.fromJson(jsonObject.toString(), GetStoryFeed.class);

        //  Log.d("readvalue",red.getDataStories().getUnRead().toString());

        String num_read= "";

        if(red.getDataStories().getUnseen()!=null&&!red.getDataStories().getUnseen().isEmpty()){

            num_read = red.getDataStories().getUnseen();
        }

        Log.d("Num_read",num_read);




        for (int i = 0; i < red.getDataStories().getStoryDataList().size(); i++) {

            if(red.getDataStories().getStoryDataList().get(i).getCategoryName()!=null&&!red.getDataStories().getStoryDataList().get(i).getCategoryName().isEmpty()){

                mListFromJson.add(red.getDataStories().getStoryDataList().get(i));
            }

        }



    }


    public GetFilters filterConversion(String jsonString){

        mJsonString = jsonString;

        Gson gson = new Gson();

        GetFilters datafromgson = gson.fromJson(mJsonString,GetFilters.class);

        Log.d("datafromgson",datafromgson.getdata().getGovernance().get(0).getName());

        return datafromgson;
    }


    public List<Filters> getFilters(String jsonString){
       mJsonString =  jsonString;
        Gson gson = new Gson();

        FilterAPIJson datafromgson = gson.fromJson(mJsonString, FilterAPIJson.class);

        Log.d("filtername_example",datafromgson.getDataList().get(0).getName());

        return datafromgson.getDataList();
    }

    public String getAuthtoken(String jsonString){
        mJsonString = jsonString;

        Gson gson = new Gson();

        Authverify datafromgson = gson.fromJson(mJsonString,Authverify.class);

        return datafromgson.getAuthData().getAuthtoke();

    }



  /*
        Gson gson = new Gson();
        GetStoryFeedMain red = gson.fromJson(mJsonObject.toString(),GetStoryFeedMain.class);




        for (int i = 0; i < red.getDataStoriesMain().getStoryDataList().size(); i++) {

            if(red.getDataStoriesMain().getStoryDataList().get(i).getCategoryName()!=null&&!red.getDataStoriesMain().getStoryDataList().get(i).getCategoryName().isEmpty()){

                nListFromJson.add(red.getDataStoriesMain().getStoryDataList().get(i));
            }

        }

        for (int i = 0; i < red2.getDataStoriesMain().getStoryDataList().size(); i++) {

            if(red2.getDataStoriesMain().getStoryDataList().get(i).getCategoryName()!=null&&!red2.getDataStoriesMain().getStoryDataList().get(i).getCategoryName().isEmpty()){

                oListFromJson.add(red2.getDataStoriesMain().getStoryDataList().get(i));
            }

        }


        NavigationActivity activity = (NavigationActivity)context;

        if(fragmenttag==1&&totalrefresh.equals("0")) {

            Fragment f = activity.getFragment(fragmenttag);

            ((HomeFragment) f).startAdapter(nListFromJson,oListFromJson);

        }else if(fragmenttag==1&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((HomeFragment)f).startRefreshAdapter(nListFromJson,oListFromJson);
        }
*/


}