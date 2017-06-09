package in.thesoup.thesoup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.Fragments.DiscoverFragment;
import in.thesoup.thesoup.Fragments.MyFeedFragment;
import in.thesoup.thesoup.GSONclasses.FeedGSON.GetStoryFeed;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.GetSingleStory;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substoryjsondata;
import in.thesoup.thesoup.GSONclasses.filters.Filterdata;
import in.thesoup.thesoup.GSONclasses.filters.GetFilters;

/**
 * Created by Jani on 07-04-2017.
 */

public class gsonConversion {


    private JSONObject mJsonObject;
    private List<StoryData> mListFromJson;
    private Substoryjsondata mSubstoryjsonData;
    private String StoryTitle ,mJsonString, followstatus;
    private List<Substories> mSubstories;
    private Filterdata filterdata;


    public void fillUI(JSONObject jsonObject,Context context,int fragmenttag,String totalrefresh) {

        mListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetStoryFeed red = gson.fromJson(jsonObject.toString(), GetStoryFeed.class);

        for (int i = 0; i < red.getStoryDataList().size(); i++) {

            mListFromJson.add(red.getStoryDataList().get(i));
        }


        MainActivity activity = (MainActivity) context;


        if(fragmenttag==0&&totalrefresh.equals("0")) {

            Fragment f = activity.getFragment(fragmenttag);

            ((DiscoverFragment) f).startAdapter(mListFromJson);

        }else if(fragmenttag==0&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((DiscoverFragment)f).startRefreshAdapter(mListFromJson);
        }


        if(fragmenttag==1&&totalrefresh.equals("0")){
            Fragment f = activity.getFragment(fragmenttag);
            ((MyFeedFragment)f).startAdapter(mListFromJson);
        }else if(fragmenttag==1&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((MyFeedFragment)f).startRefreshAdapter(mListFromJson);
        }


    }
          // if(f instanceof DiscoverFragment){



           //}



        /*if(context instanceof feedActivity){
            feedActivity activity = (feedActivity)context;
            activity.startAdapter(mListFromJson);
        }
    }*/

    public void fillStoryUI(JSONObject jsonObject, Context context){

        mSubstories = new ArrayList<>();

        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetSingleStory redstory = gson.fromJson(jsonObject.toString(),GetSingleStory.class);

        StoryTitle = redstory.getdata().getStoryName();

       for(int i=0;i<redstory.getdata().getSubstories().size();i++){

           mSubstories.add(redstory.getdata().getSubstories().get(i));
        }

        Log.d("substories",mSubstories.toString());


        DetailsActivity activity = (DetailsActivity) context;

        activity.startAdapter(mSubstories,StoryTitle);






    }

    public GetFilters filterConversion(String jsonString){

        mJsonString = jsonString;

        Gson gson = new Gson();

        GetFilters datafromgson = gson.fromJson(mJsonString,GetFilters.class);

        Log.d("datafromgson",datafromgson.getdata().getGovernance().get(0).getName());

        return datafromgson;
    }






}
