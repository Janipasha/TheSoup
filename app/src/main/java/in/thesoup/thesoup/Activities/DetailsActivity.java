package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Adapters.SingleStoryAdapter;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsStory;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 09-04-2017.
 */

public class DetailsActivity extends AppCompatActivity {

    private List<Substories> mSubstories;
    //private List<Substoryjsondata> substoryjsondataList;
    private RecyclerView SingleStoryView;
    private SingleStoryAdapter nSingleStoryAdapter;
    private String StoryTitle, followStatus,StoryId;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SharedPreferences pref;
    HashMap<String,String> params;
    //private Tracker mTracker;
   // private AnalyticsApplication application;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstorydetails);

        //application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbardetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        Bundle bundle = getIntent().getExtras();
        StoryId = bundle.getString("story_id");
        followStatus = bundle.getString("followstatus");
        
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        Log.d("followstatus details1",followStatus);

        if(TextUtils.isEmpty(followStatus)){
            followStatus = "0";
        }

        Log.d("StoryId",StoryId);
        Log.d("followstatus details2",followStatus);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        mSubstories = new ArrayList<>();


        SingleStoryView = (RecyclerView) findViewById(R.id.list_story);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SingleStoryView.setLayoutManager(layoutManager);
        //nSingleStoryAdapter = new SingleStoryAdapter(mSubstories,StoryTitle,followStatus,DetailsActivity.this,StoryId);
        //SingleStoryView.setAdapter(nSingleStoryAdapter);
        SingleStoryView.setHasFixedSize(true);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);

            }
        };


        
        SingleStoryView.addOnScrollListener(scrollListener);

         params = new HashMap<>();
        params.put("page", "0");
        params.put("story_id", StoryId);

        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {


            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            params.put("auth_token", pref.getString("auth_token", null));
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);



            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        StoryTitle = " ";
        //random value for function to run else throws error in stringValue htm


        nSingleStoryAdapter = new SingleStoryAdapter(mSubstories,StoryTitle,followStatus,DetailsActivity.this,StoryId);
        SingleStoryView.setAdapter(nSingleStoryAdapter);

        





    }

    private void loadNextDataFromApi(int offset) {

        String page = String.valueOf(offset);

        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {

            params.put("page",page);
            params.put("story_id", StoryId);


            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page",page);
            params.put("story_id", StoryId);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this,params);


            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public void startAdapter(List<Substories> mSubstories, String StoryTitle){


        nSingleStoryAdapter.refreshData(mSubstories,StoryTitle);
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        application.sendScreenName(mTracker, SoupContract.COLLECTION_VIEWED);

        if(pref.contains(SoupContract.FB_ID)){

            Log.d("fb_id",pref.getString(SoupContract.FB_ID,null));

            String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
            application.sendEventCollectionUser(mTracker, SoupContract.PAGE_VIEW, SoupContract.COLLECTION_VIEWED,
                    SoupContract.COLLECTION_PAGE,StoryId,StoryTitle,pref.getString(SoupContract.FB_ID,null),
                    pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));
        }else {

            application.sendEventCollection(mTracker, SoupContract.PAGE_VIEW, SoupContract.COLLECTION_VIEWED, SoupContract.COLLECTION_PAGE,StoryId,StoryTitle);
        }

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 36) {
            Log.d("result in Details", "worked");
            nSingleStoryAdapter.followstory();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void demo(String storyId ) {
       //TODO:
        /* Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("story_id",storyId);
        intent.putExtra("activity","1");
        startActivityForResult(intent,36);*/
    }

    public void DetailsActivitydemo(String mfollowStatus){
        followStatus = mfollowStatus;
        nSingleStoryAdapter.refreshFollowStatus(followStatus);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}