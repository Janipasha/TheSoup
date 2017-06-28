package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Adapters.ExpandableListAdapter;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
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
    // private RecyclerView SingleStoryView;
    private ExpandableListView SingleStoryView;
    //private SingleStoryAdapter nSingleStoryAdapter;
    private ExpandableListAdapter nSingleStoryAdapter;
    private String StoryTitle, followStatus, StoryId;
    private EndlessScrollListener scrollListener;
    private SharedPreferences pref;
    HashMap<String, String> params;
    private int fragmenttag;
    private TextView mheading,categoryname;
    private Button followbutton;
    private String storyColour,categoryName;
    private int restartactivitystatus = 0;
    private ProgressBar progress;
    //private Tracker mTracker;
    // private AnalyticsApplication application;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // setContentView(R.layout.getstorydetails);

        setContentView(R.layout.expandabletext);

        //application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbardetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        progress = (ProgressBar)findViewById(R.id.progressbarstory);
        progress.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        StoryId = bundle.getString("story_id");
        followStatus = bundle.getString("followstatus");
        fragmenttag = getIntent().getIntExtra("fragmenttag", 0);

        mheading = (TextView) findViewById(R.id.story_title_header1);
        categoryname = (TextView)findViewById(R.id.category);
        followbutton = (Button) findViewById(R.id.followbutton_header1);



        if (Build.VERSION.SDK_INT >= 24) {
            StoryTitle = String.valueOf(Html.fromHtml(bundle.getString("storytitle"), Html.FROM_HTML_MODE_LEGACY));
            categoryName=String.valueOf(Html.fromHtml(bundle.getString("category"),Html.FROM_HTML_MODE_LEGACY));
        } else {

            StoryTitle = String.valueOf(Html.fromHtml(bundle.getString("storytitle")));
            categoryName = String.valueOf(Html.fromHtml(bundle.getString("category")));
        }

        if(categoryName!=null&&!categoryName.isEmpty()) {

            categoryname.setText(categoryName);
        }

        mheading.setText(StoryTitle);


        if (bundle.getString("hex_colour") != null && !bundle.getString("hex_colour").isEmpty()) {

            storyColour = bundle.getString("hex_colour");


        }

        if (storyColour != null && !storyColour.isEmpty()) {
            //DrawableCompat.setTint(followbutton.getDrawable(),Color.parseColor("#" + storyColour));
            followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
            categoryname.setTextColor(Color.parseColor("#" + storyColour));
        }


        pref = PreferenceManager.getDefaultSharedPreferences(this);

        Log.d("followstatus details1", followStatus);

        if (TextUtils.isEmpty(followStatus)) {
            followStatus = "0";
        }

        followButtonStatus();

        Log.d("StoryId", StoryId);
        Log.d("followstatus details2", followStatus);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proxima-nova-black.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        mSubstories = new ArrayList<>();


        //SingleStoryView = (RecyclerView) findViewById(R.id.list_story);
        SingleStoryView = (ExpandableListView) findViewById(R.id.lvExp);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //SingleStoryView.setLayoutManager(layoutManager);
        //******************************************************

        //SingleStoryView.setHasFixedSize(true);

        scrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount, AbsListView view) {

                loadNextDataFromApi(page);

            }
        };


        SingleStoryView.setOnScrollListener(scrollListener);
        //SingleStoryView.addOnScrollListener(scrollListener);

        params = new HashMap<>();
        params.put("page", "0");
        params.put("story_id", StoryId);

        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {

            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);


            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);


            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        //nSingleStoryAdapter = new SingleStoryAdapter(mSubstories,StoryTitle,followStatus,DetailsActivity.this,StoryId,fragmenttag);
        nSingleStoryAdapter = new ExpandableListAdapter(this, mSubstories, storyColour);
        SingleStoryView.setAdapter(nSingleStoryAdapter);

        SingleStoryView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String ArticleURL = mSubstories.get(groupPosition).getArticles().get(childPosition).getUrl();

                Intent intent = new Intent(DetailsActivity.this, ArticleWebViewActivity.class);
                intent.putExtra("ArticleURL", ArticleURL);
                intent.putExtra("substory_id",mSubstories.get(groupPosition).getSubstoryId());
                startActivity(intent);

                return true;
            }
        });




    }

    private void followButtonStatus() {

        if (followStatus.equals("1")) {
            followbutton.setText("FOLLOWING");
            if (storyColour != null && !storyColour.isEmpty()) {
                //DrawableCompat.setTint(followbutton.getDrawable(),Color.parseColor("#" + storyColour));
                followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
            }

          //  LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //llp.setMargins(0, 50, 0, 0); // llp.setMargins(left, top, right, bottom);
            //followbutton.setLayoutParams(llp);

        } else if (followStatus.equals("0")) {
            followbutton.setText("FOLLOW");
            if (storyColour != null && !storyColour.isEmpty()) {
               // DrawableCompat.setTint(followbutton.getDrawable(),Color.parseColor("#" + storyColour));
                followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
            }
        }
    }

    private void loadNextDataFromApi(int offset) {

        String page = String.valueOf(offset);

        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {

            params.put("page", page);
            params.put("story_id", StoryId);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);


            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", page);
            params.put("story_id", StoryId);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, params);


            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public void startAdapter(List<Substories> mSubstories, String StoryTitle) {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        // nSingleStoryAdapter.refreshData(mSubstories,StoryTitle);

        nSingleStoryAdapter.refreshData((mSubstories));
        SingleStoryView.expandGroup(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            if(restartactivitystatus==1){
            // do something on back.
            Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
            startActivity(intent);
                return true;
            }else{
                return super.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
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
            //Todo: nSingleStoryAdapter.followstory();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void DetailsActivitydemo(String mfollowStatus) {

        if (!(followStatus.equals(mfollowStatus))) {
            followStatus = mfollowStatus;
            followButtonStatus();

            restartactivitystatus = 1;

        }


        followStatus = mfollowStatus;

        //Todo: nSingleStoryAdapter.refreshFollowStatus(followStatus);


    }

    public void onClick(View v) {

        int i = 0; //constant value

        if (v == followbutton) {
            if (followStatus.equals("") || followStatus.equals("0")) {


                //NetworkUtilsFollowUnFollow follow = new NetworkUtilsFollowUnFollow(context,mString);
                HashMap<String, String> params = new HashMap<>();
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("story_id", StoryId);
                //application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_FOLLOW, SoupContract.COLLECTION_PAGE,storyTitle,String.valueOf(clickStoryId),pref.getString(SoupContract.FB_ID,null),pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

                NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(this, params);
                followrequest.followRequest(i, fragmenttag);
            } else if (followStatus.equals("1")) {
                //application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_UNFOLLOW, SoupContract.COLLECTION_PAGE,storyTitle,String.valueOf(clickStoryId),pref.getString(SoupContract.FB_ID,null),pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));
                HashMap<String, String> params = new HashMap<>();
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("story_id", StoryId);
                NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(this, params);
                unFollowrequest.unFollowRequest(i, fragmenttag);
            }
        }


    }

    public void stopProgress() {
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    }
}