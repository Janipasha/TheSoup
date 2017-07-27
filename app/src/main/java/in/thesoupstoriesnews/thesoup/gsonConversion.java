package in.thesoupstoriesnews.thesoup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.DetailsActivity;
import in.thesoupstoriesnews.thesoup.Activities.MainActivity;
import in.thesoupstoriesnews.thesoup.Fragments.DiscoverFragment;
import in.thesoupstoriesnews.thesoup.Fragments.MyFeedFragment;
import in.thesoupstoriesnews.thesoup.GSONclasses.AuthVerify.Authverify;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.GetStoryFeed;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.GetSingleStory;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Substoryjsondata;
import in.thesoupstoriesnews.thesoup.GSONclasses.filters.Filterdata;
import in.thesoupstoriesnews.thesoup.GSONclasses.filters.GetFilters;
import in.thesoupstoriesnews.thesoup.GSONclasses.filters1.FilterAPIJson;
import in.thesoupstoriesnews.thesoup.GSONclasses.filters1.Filters;

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

        Log.d("readvalue",red.getDataStories().getUnRead().toString());

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
            ((MyFeedFragment)f).startAdapter(mListFromJson,num_read);
        }else if(fragmenttag==1&&totalrefresh.equals("1")){
            Fragment f = activity.getFragment(fragmenttag);
            ((MyFeedFragment)f).startRefreshAdapter(mListFromJson,num_read);
        }


    }


    public void fillStoryUI(JSONObject jsonObject, Context context){

        mSubstories = new ArrayList<>();

        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetSingleStory redstory = gson.fromJson(jsonObject.toString(),GetSingleStory.class);

        StoryTitle = redstory.getdata().getStoryName();

       for(int i=0;i<redstory.getdata().getSubstories().size();i++){

           mSubstories.add(redstory.getdata().getSubstories().get(i));
           Log.e("Substory heading",mSubstories.get(i).getSubstoryName());
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






}
