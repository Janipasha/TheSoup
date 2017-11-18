package in.thesoup.thesoupstoriesnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.CategoryActivity;
import in.thesoup.thesoupstoriesnews.activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.activities.EndlessRecyclerView;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.adapters.CategoryFeedAdapter;
import in.thesoup.thesoupstoriesnews.fragments.DiscoverFragmentMain;
import in.thesoup.thesoupstoriesnews.fragments.FollowingFragment;
import in.thesoup.thesoupstoriesnews.fragments.HomeFragment;
import in.thesoup.thesoupstoriesnews.fragments.HomeFragment1;
import in.thesoup.thesoupstoriesnews.gsonclasses.AuthVerify.Authverify;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSON.GetStoryFeed;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSON.StoryData;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.GetStoryFeedMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.GetStoryFeedMainHome;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.StoryDataMainHome;
import in.thesoup.thesoupstoriesnews.gsonclasses.FollowingGSON.GetStoryFeedFollowing;
import in.thesoup.thesoupstoriesnews.gsonclasses.FollowingGSON.StoryDataFollowing;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.GetSingleStory;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Substoryjsondata;
import in.thesoup.thesoupstoriesnews.gsonclasses.filters.Filterdata;
import in.thesoup.thesoupstoriesnews.gsonclasses.filters.GetFilters;
import in.thesoup.thesoupstoriesnews.gsonclasses.filters1.FilterAPIJson;
import in.thesoup.thesoupstoriesnews.gsonclasses.filters1.Filters;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsHome;

import static in.thesoup.thesoupstoriesnews.R.id.section;

/**
 * Created by Jani on 07-04-2017.
 */

public class gsonConversion {


    private JSONObject mJsonObject,nJsonObject;
    private List<StoryData> mListFromJson;
    private List<StoryDataMainHome> ListFromJson;
    private List<StoryDataMain> nListFromJson,oListFromJson;
    private List<StoryDataFollowing> pListFromJson;
    private Substoryjsondata mSubstoryjsonData;
    private String StoryTitle ,mJsonString, followstatus;
    private List<Substories> mSubstories;
    private Filterdata filterdata;
    private HashMap <String,String> params;
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

                if(fragmenttag==0&&totalrefresh.equals("0")) {

                    Fragment f = activity.getFragment(fragmenttag);

                    ((HomeFragment) f).startAdapterfirsttime(nListFromJson,oListFromJson,followcount,followUpdateCount);

                }else if(fragmenttag==0&&totalrefresh.equals("1")){
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
            if(activity!=null){
                if(fragmenttag==0&&totalrefresh.equals("0")) {
                    Fragment f = activity.getFragment(fragmenttag);
                    ((HomeFragment) f).startAdapterAfterPagination(oListFromJson);
                }else if(fragmenttag==0&&totalrefresh.equals("1")){
                    Fragment f = activity.getFragment(fragmenttag);
                    ((HomeFragment)f).startAdaptersecondcall(oListFromJson);
                }

            }

            }
        }

        public void fillHome(JSONObject jsonObject, Context context, int fragmenttag, String totalrefresh, String section, EndlessRecyclerView scrollListener) {

            ListFromJson = new ArrayList<>();
            nJsonObject = jsonObject;

            Gson gson = new Gson();
            GetStoryFeedMainHome red = gson.fromJson(jsonObject.toString(), GetStoryFeedMainHome.class);

            if (red.getDataStoriesMain().getStories().get(0).getType().equals("7")) {

                if (context != null) {

                    scrollListener.changeoffset(Integer.valueOf(section)-1);

                   // NetworkUtilsHome networkutilsToken = new NetworkUtilsHome(context, ListFromJson, params, scrollListener);
                   // networkutilsToken.getFeed2(0, totalrefresh);
                }
            } else {

                for (int i = 0; i < red.getDataStoriesMain().getStories().size(); i++) {
                    ListFromJson.add(red.getDataStoriesMain().getStories().get(i));
                }

                NavigationActivity activity = (NavigationActivity) context;


                    if (activity != null) {
                        if (fragmenttag == 0 && totalrefresh.equals("0")) {
                            Fragment f = activity.getFragment(fragmenttag);
                            ((HomeFragment1) f).startAdapterfirsttime(ListFromJson);
                        } else if (fragmenttag == 0 && totalrefresh.equals("1")) {
                            Fragment f = activity.getFragment(fragmenttag);
                            ((HomeFragment1) f).startAdapter(ListFromJson);
                        }



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

       /* NavigationActivity activity = (NavigationActivity)context;

        if(activity!=null){

            if(fragmenttag==0&&totalrefresh.equals("0")) {

                Fragment f = activity.getFragment(fragmenttag);

                ((DiscoverFragmentMain) f).startAdapter(nListFromJson);

            }
            else if(fragmenttag==0&&totalrefresh.equals("1")){
                Fragment f = activity.getFragment(fragmenttag);
                ((DiscoverFragmentMain)f).startRefreshAdapter(nListFromJson);
            }*/

        CategoryActivity categoryActivity = (CategoryActivity)context;

        if(categoryActivity!=null&&totalrefresh.equals("1")){
            categoryActivity.startRefreshAdapter(nListFromJson);
        }else if(categoryActivity!=null&&totalrefresh.equals("0")){
            categoryActivity.startAdapter(nListFromJson);
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
        if(fragmenttag==1&&totalrefresh.equals("0")) {
            if(activity!=null){
                Fragment f = activity.getFragment(fragmenttag);
                ((FollowingFragment) f).startAdapter(pListFromJson,followunseen);}
        }else if(fragmenttag==1&&totalrefresh.equals("1")){
            if(activity!=null){
                Fragment f = activity.getFragment(fragmenttag);
                ((FollowingFragment)f).startRefreshAdapter(pListFromJson,followunseen);
            }
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

        if(activity!=null){

            if(totalRefresh.equals("0")) {

                activity.startAdapter(mSubstories, StoryTitle,StoryId);
            }else if (totalRefresh.equals("1")){
                activity.startRefreshAdapter(mSubstories,StoryId);
            }

            activity.sendFirebaseExpandEvent(mSubstories);

        }


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
