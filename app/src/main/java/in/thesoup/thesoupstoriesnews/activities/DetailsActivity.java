package in.thesoup.thesoupstoriesnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.uxcam.UXCam;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.adapters.DetailsmainAdapter;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsClick;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsStory;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtil;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.string.no;


/**
 * Created by Jani on 09-04-2017.
 */

public class DetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<Substories> mSubstories;
    private RecyclerView SingleStoryView;
    private DetailsmainAdapter nSingleStoryAdapter;
    private String StoryTitle, followStatus, StoryId,scrollToPosition = "0";
    private EndlessRecyclerView scrollListener;
    private SharedPreferences pref;
    HashMap<String, String> mparams;
    private int fragmenttag;
    private TextView mheading, categoryname;
    private Button followbutton;
    private String storyColour, categoryId;
    private int restartactivitystatus = 0,gotoposition =0;
    private ProgressBar progress;
    private ImageButton mBack, tickfollow;
    private ImageView filtericon;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RelativeLayout relativeLayout, relativeLayouttick;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String totalRefresh = "0";
    private HashMap<String, String> expandedCount;
    private CleverTapAPI cleverTap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }

        UXCam.startWithKey(getString(R.string.uxcam_id));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PrefUtil prefutil = new PrefUtil(this);
        if(prefutil.getEmail()!=null&&!prefutil.getEmail().isEmpty()){
            UXCam.tagUsersName(prefutil.getEmail());
        }
        setContentView(R.layout.expandabletext);
        if (getIntent().getStringExtra("fragmentPosition") != null) {
            StoryTitle = getIntent().getStringExtra("story_name");
            followStatus = "1";
            StoryId = getIntent().getStringExtra("story_id");
            fragmenttag = 0;
            categoryId = getIntent().getStringExtra("cat_id");
            storyColour = getIntent().getStringExtra("hex_colour");
            restartactivitystatus = 2;

            HashMap<String,String> nparams = new HashMap<>();
            nparams.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
            nparams.put("id",StoryId);
            nparams.put("type","stories");

            NetworkUtilsClick Click = new NetworkUtilsClick(this,nparams);
            try {
                Click.sendClick();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if(getIntent().getAction()!=null){
            Intent intent = getIntent();
            Uri data = intent.getData();

            Log.d("URI",data.toString());
            restartactivitystatus = 2;

            StoryTitle = data.getQueryParameter("story_name");

            followStatus = data.getQueryParameter("follow");
            categoryId = data.getQueryParameter("cat_id");
            storyColour = data.getQueryParameter("hex_colour");
            StoryId = data.getQueryParameter("story_id");

            Log.e("uri title",StoryTitle+ "\n"+ followStatus +"\n"+categoryId + "\n"+storyColour+"\n"+StoryId);

            HashMap<String,String> nparams = new HashMap<>();
            nparams.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
            nparams.put("id",StoryId);
            nparams.put("type","stories");

            NetworkUtilsClick Click = new NetworkUtilsClick(this,nparams);
            try {
                Click.sendClick();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else {
                Bundle bundle = getIntent().getExtras();
                StoryId = bundle.getString("story_id");
                followStatus = bundle.getString("followstatus");
                fragmenttag = getIntent().getIntExtra("fragmenttag", 0);
                if (bundle.getString("hex_colour") != null && !bundle.getString("hex_colour").isEmpty()) {
                    storyColour = bundle.getString("hex_colour");
                }

                if(bundle.getString("cardnumber")!=null&& !bundle.getString("cardnumber").isEmpty()){
                    scrollToPosition=bundle.getString("cardnumber");
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    StoryTitle = String.valueOf(Html.fromHtml(bundle.getString("storytitle"), Html.FROM_HTML_MODE_LEGACY));
                    categoryId = String.valueOf(Html.fromHtml(bundle.getString("category_id"), Html.FROM_HTML_MODE_LEGACY));
                } else {

                    StoryTitle = String.valueOf(Html.fromHtml(bundle.getString("storytitle")));
                    categoryId = String.valueOf(Html.fromHtml(bundle.getString("category_id")));
                }
            }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#" + storyColour));
        }

        progress = (ProgressBar) findViewById(R.id.progressbarstory);
        progress.setVisibility(View.GONE);
        filtericon = (ImageView) findViewById(R.id.filtericon);
        relativeLayout = (RelativeLayout) findViewById(R.id.head);
        relativeLayouttick = (RelativeLayout) findViewById(R.id.detailslayout);

        tickfollow = (ImageButton) findViewById(R.id.tickmark_follow);
        mheading = (TextView) findViewById(R.id.story_title_header1);
        followbutton = (Button) findViewById(R.id.followbutton_header1);

        if (categoryId != null && !categoryId.isEmpty()) {
            int filter = getDrawable(categoryId);
            filtericon.setImageResource(filter);
            filtericon.setColorFilter(Color.parseColor("#ffffff"));
        }

        mheading.setText(StoryTitle);

        if (storyColour != null && !storyColour.isEmpty()) {
            followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
            relativeLayout.setBackgroundColor(Color.parseColor("#" + storyColour));
        } else {
            followbutton.setBackgroundColor(Color.parseColor("#000000"));
        }

        Bundle params = new Bundle();
        params.putString("screen_name", "story_screen");
        params.putString("category", "screen_view");
        params.putString("collection_id", StoryId);
        params.putString("follow_status", followStatus);
        params.putString("collection_name", StoryTitle);
        mFirebaseAnalytics.logEvent("viewed_screen_story", params);

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "story_screen");
        nparams.put("category", "screen_view");
        nparams.put("collection_id", StoryId);
        if(followStatus!=null&&!followStatus.isEmpty()){
            nparams.put("follow_status", followStatus);
        }else{
            nparams.put("follow_status","0");
        }
        nparams.put("collection_name", StoryTitle);
        cleverTap.event.push("viewed_screen_story", nparams);




        if (followStatus!=null&&!followStatus.isEmpty()) {}else {
            followStatus="0";
        }
        followButtonStatus();

        Log.d("StoryId", StoryId);
        Log.d("followstatus details2", followStatus);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/montserrat-bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mSubstories = new ArrayList<>();

        SingleStoryView = (RecyclerView) findViewById(R.id.lvExp);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SingleStoryView.setLayoutManager(layoutManager);
        mparams = new HashMap<>();
        nSingleStoryAdapter = new DetailsmainAdapter(this, mSubstories, storyColour);
        SingleStoryView.setAdapter(nSingleStoryAdapter);
        SingleStoryView.setHasFixedSize(true);

        scrollListener = new EndlessRecyclerView(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                loadNextDataFromApi(current_page);
            }

        };

        SingleStoryView.addOnScrollListener(scrollListener);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_details);
        swipeRefreshLayout.setOnRefreshListener(this);


       NetworkCallDetails();

        expandedCount = new HashMap<>();

        relativeLayouttick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow();
            }
        });

    }

    private void NetworkCallDetails() {

        if (pref.getString(SoupContract.AUTH_TOKEN, null) != null && !pref.getString(SoupContract.AUTH_TOKEN, null).isEmpty()) {
            mparams.put("page", "0");
            mparams.put("story_id", StoryId);
            mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            Log.e("details story page",mparams.get("page"));

            if (swipeRefreshLayout.isRefreshing()) {
                progress.setVisibility(View.GONE);
            } else {
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0);
            }

            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams, totalRefresh);
            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mparams.put("page", "0");
            mparams.put("story_id", StoryId);
            progress.setVisibility(View.VISIBLE);


            progress.setProgress(0);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams, totalRefresh);
            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void followButtonStatus() {

        if (followStatus.equals("1")) {

            followbutton.setBackgroundColor(Color.parseColor("#000000"));
            followbutton.setTextColor(Color.parseColor("#ffffff"));
            followbutton.setText("FOLLOWING");
            followbutton.setVisibility(View.GONE);
            relativeLayouttick.setVisibility(View.VISIBLE);
            tickfollow.setVisibility(View.VISIBLE);
        } else if (followStatus.equals("0")) {
            tickfollow.setVisibility(View.GONE);
            relativeLayouttick.setVisibility(View.GONE);
            followbutton.setVisibility(View.VISIBLE);
            followbutton.setText("Follow This Story");
            if (storyColour != null && !storyColour.isEmpty()) {
                followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
                followbutton.setTextColor(Color.parseColor("#ffffff"));
            }
        } else if (followStatus == null && followStatus.isEmpty()) {
            followbutton.setText("Follow This Story");
            if (storyColour != null && !storyColour.isEmpty()) {
                followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
                followbutton.setTextColor(Color.parseColor("#ffffff"));


            }
        }
    }

    private void loadNextDataFromApi(int offset) {

        totalRefresh = "0";

        String page = String.valueOf(offset+1);

        if (pref.getString(SoupContract.AUTH_TOKEN, null) != null && !pref.getString(SoupContract.AUTH_TOKEN, null).isEmpty()) {

            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            mparams.put("page", page);
            mparams.put("story_id", StoryId);
            Log.e("details story page",mparams.get("page"));
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams, totalRefresh);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            mparams.put("page", page);
            mparams.put("story_id", StoryId);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams, totalRefresh);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onRefresh() {
        totalRefresh = "1";
        scrollListener.changeoffset(0);
        swipeRefreshLayout.setRefreshing(true);
        NetworkCallDetails();
    }

    public void startRefreshAdapter(List<Substories> mSubstories, String StoryId) {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        // nSingleStoryAdapter.refreshData(mSubstories,StoryTitle);

        nSingleStoryAdapter.totalRefreshData((mSubstories), StoryId);
        swipeRefreshLayout.setRefreshing(false);
    }


    public void startAdapter(List<Substories> mSubstories, String StoryTitle, String StoryId) {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        // nSingleStoryAdapter.refreshData(mSubstories,StoryTitle);

        nSingleStoryAdapter.refreshData((mSubstories), StoryId);
        if(gotoposition==0){
            SingleStoryView.scrollToPosition(Integer.valueOf(scrollToPosition));
        gotoposition = 1;
        }

         swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "collection_screen");
            mparams.putString("category", "tap"); //(only if possible)
            mFirebaseAnalytics.logEvent("tap_back", mparams);

            HashMap<String,Object> nparams = new HashMap<>();
            nparams.put("screen_name", "collection_screen");
            nparams.put("category", "tap"); //(only if possible)
            cleverTap.event.push("tap_back", nparams);

            if (restartactivitystatus == 1) {

                Intent intent = new Intent();
                intent.putExtra("storyId", StoryId);
                setResult(1,intent);
                finish();


                return true;
            } else if(restartactivitystatus==2) {

                Intent intent = new Intent(this,NavigationActivity.class);
                startActivity(intent);
                finish();

            } else{
                return super.onKeyDown(keyCode, event);

            }

        }
            return super.onKeyDown(keyCode, event);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 36) {
            Log.d("result in Details", "worked");
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void DetailsActivitydemo(String mfollowStatus) {

     if(mfollowStatus.equals("1")){
         Toast.makeText(this, "Now following the Story", Toast.LENGTH_SHORT).show();
     }

        if (!(followStatus.equals(mfollowStatus))) {
            followStatus = mfollowStatus;
            followButtonStatus();
            restartactivitystatus = 1;

        }

        followStatus = mfollowStatus;
        // CVIPUL Analytics
        // TODO : Verify follow event, add collection location if possible
        Bundle params = new Bundle();
        params.putString("screen_name", "collection_screen"); // "myfeed / discover"
        params.putString("collection_id", StoryId);
        params.putString("follow_status", followStatus);
        params.putString("collection_name", StoryTitle);
        params.putString("category", "conversion");
        if (mfollowStatus.equals("1")) {
            mFirebaseAnalytics.logEvent("add", params);
        } else if (mfollowStatus.equals("0")) {
            mFirebaseAnalytics.logEvent("remove", params);
        }

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "collection_screen"); // "myfeed / discover"
        nparams.put("collection_id", StoryId);
        nparams.put("follow_status", followStatus);
        nparams.put("collection_name", StoryTitle);
        nparams.put("category", "conversion");
        if (mfollowStatus.equals("1")) {
           cleverTap.event.push("add", nparams);
        } else if (mfollowStatus.equals("0")) {
            cleverTap.event.push("remove", nparams);
        }




    }

    public void onClick(View v) {



        if (v == followbutton || v == tickfollow || v == relativeLayouttick) {
            // TODO : Verify follow event, add collection location if possible
          follow();
        }


    }

    public void follow(){
        int i = 0; //constant value
        Bundle params = new Bundle();
        params.putString("screen_name", "collection_screen"); // "myfeed / discover"
        params.putString("collection_id", StoryId);
        params.putString("collection_name", StoryTitle);
        params.putString("category", "tap");

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "collection_screen"); // "myfeed / discover"
        nparams.put("collection_id", StoryId);
        nparams.put("collection_name", StoryTitle);
        nparams.put("category", "tap");

        if (followStatus.equals("") || followStatus.equals("0")) {
            mFirebaseAnalytics.logEvent("tap_add", params);
            cleverTap.event.push("tap_add",nparams);
            HashMap<String, String> mparams = new HashMap<>();
            mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            mparams.put("story_id", StoryId);
            NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(this, mparams);
            followrequest.followRequest(i, fragmenttag);
        } else if (followStatus.equals("1")) {
            mFirebaseAnalytics.logEvent("tap_remove", params);
            cleverTap.event.push("tap_remove",nparams);
            HashMap<String, String> mparams = new HashMap<>();
            mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            mparams.put("story_id", StoryId);
            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(this, mparams);
            unFollowrequest.unFollowRequest(i, fragmenttag);
        }
    }


    public int getDrawable(String filter) {

        if (filter.equals("1")) {
            return R.drawable.politics;
        } else if (filter.equals("2")) {
            return R.drawable.diplomacy;

        } else if (filter.equals("3")) {
            return R.drawable.justice;

        } else if (filter.equals("4")) {
            return R.drawable.religion;
        } else if (filter.equals("5")) {
            return R.drawable.sports;
        } else if (filter.equals("7")) {
            return R.drawable.nature;
        } else if (filter.equals("9")) {
            return R.drawable.health;
        } else if (filter.equals("10")) {
            return R.drawable.science;
        } else if (filter.equals("11")) {
            return R.drawable.human_rights;
        } else if (filter.equals("12")) {
            return R.drawable.entertainment;
        } else if (filter.equals("15")) {
            return R.drawable.accident;
        } else if (filter.equals("16")) {
            return R.drawable.economy_business;
        } else if (filter.equals("17")) {
            return R.drawable.jobs;
        } else if (filter.equals("18")) {
            return R.drawable.education;
        } else if (filter.equals("19")) {
            return R.drawable.people;
        }

        return 0;


    }
    public void stopProgress() {
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    }


    public void sendFirebaseExpandEvent(List<Substories> mSubstories) {
         if(expandedCount.containsKey(String.valueOf(0))){

        }else{

        if(mSubstories.size()>0) {
            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "collection_screen"); // "myfeed / discover"
            mparams.putString("card_type", "subcollection");
            String substoryTitle = mSubstories.get(0).getSubstoryName();
            String upToNCharacters = substoryTitle.substring(0, Math.min(substoryTitle.length(), 100));
            mparams.putString("subcollection_name", upToNCharacters);
            mparams.putString("category", "conversion");

            HashMap<String,Object> nparams = new HashMap<>();
            nparams.put("screen_name", "collection_screen"); // "myfeed / discover"
            nparams.put("card_type", "subcollection");
             nparams.put("subcollection_name", upToNCharacters);
            nparams.put("category", "conversion");


            if (followStatus.equals("1")) {
                mparams.putString("subscription_state", "added");
                nparams.put("subscription_state", "added");
            } else {
                mparams.putString("subscription_state", "not added");
                nparams.put("subscription_state", "not added");
            }

            mFirebaseAnalytics.logEvent("explore_subcollection", mparams);
            cleverTap.event.push("explore_subcollection",nparams);
            expandedCount.put(String.valueOf(0),String.valueOf(0));
        }

    }}
}

