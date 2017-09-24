package in.thesoupstoriesnews.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Adapters.DetailsmainAdapter;
import in.thesoupstoriesnews.thesoup.Adapters.ExpandableListAdapter;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsStory;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
import me.toptas.fancyshowcase.FancyShowCaseView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.thesoupstoriesnews.thesoup.R.id.bottomline;
import static in.thesoupstoriesnews.thesoup.R.id.detailslayout;
import static in.thesoupstoriesnews.thesoup.R.id.showmore;

/**
 * Created by Jani on 09-04-2017.
 */

public class DetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<Substories> mSubstories;
    private RecyclerView SingleStoryView;
    private DetailsmainAdapter nSingleStoryAdapter;
    private String StoryTitle, followStatus, StoryId;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SharedPreferences pref;
    HashMap<String, String> mparams;
    private int fragmenttag;
    private TextView mheading, categoryname;
    private Button followbutton;
    private String storyColour, categoryName;
    private int restartactivitystatus = 0;
    private ProgressBar progress;
    private ImageButton mBack, tickfollow;
    private ImageView filtericon;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RelativeLayout relativeLayout, relativeLayouttick;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String totalRefresh = "0";
    private HashMap<String, String> expandedCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.expandabletext);
        if (getIntent().getStringExtra("fragmentPosition") != null) {
            StoryTitle = getIntent().getStringExtra("story_name");
            followStatus = "1";
            StoryId = getIntent().getStringExtra("story_id");
            fragmenttag = 0;
            categoryName = getIntent().getStringExtra("cat_name");
            storyColour = getIntent().getStringExtra("hex_colour");
            restartactivitystatus = 1;
        } else {
            Bundle bundle = getIntent().getExtras();
            StoryId = bundle.getString("story_id");
            followStatus = bundle.getString("followstatus");
            fragmenttag = getIntent().getIntExtra("fragmenttag", 0);
            if (bundle.getString("hex_colour") != null && !bundle.getString("hex_colour").isEmpty()) {
                storyColour = bundle.getString("hex_colour");
            }

            if (Build.VERSION.SDK_INT >= 24) {
                StoryTitle = String.valueOf(Html.fromHtml(bundle.getString("storytitle"), Html.FROM_HTML_MODE_LEGACY));
                categoryName = String.valueOf(Html.fromHtml(bundle.getString("category"), Html.FROM_HTML_MODE_LEGACY));
            } else {

                StoryTitle = String.valueOf(Html.fromHtml(bundle.getString("storytitle")));
                categoryName = String.valueOf(Html.fromHtml(bundle.getString("category")));
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#"+storyColour));
        }

        progress = (ProgressBar) findViewById(R.id.progressbarstory);
        progress.setVisibility(View.GONE);
        filtericon = (ImageView) findViewById(R.id.filtericon);
        relativeLayout = (RelativeLayout) findViewById(R.id.head);
        relativeLayouttick = (RelativeLayout) findViewById(R.id.detailslayout);
        tickfollow = (ImageButton) findViewById(R.id.tickmark_follow);
        mheading = (TextView) findViewById(R.id.story_title_header1);
        followbutton = (Button) findViewById(R.id.followbutton_header1);

        if (categoryName != null && !categoryName.isEmpty()) {
            int filter = getDrawable(categoryName);
            filtericon.setImageResource(filter);
            filtericon.setColorFilter(Color.parseColor("#cbffffff"));
        }

        mheading.setText(StoryTitle);

        if (storyColour != null && !storyColour.isEmpty()) {
            followbutton.setBackgroundColor(Color.parseColor("#" + storyColour));
            relativeLayout.setBackgroundColor(Color.parseColor("#" + storyColour));
        } else {
            followbutton.setBackgroundColor(Color.parseColor("#000000"));
        }

        // TODO : Verify view collection screen
        Bundle params = new Bundle();
        params.putString("screen_name", "collection_screen");
        params.putString("category", "screen_view");
        params.putString("collection_id", StoryId);
        params.putString("follow_status", followStatus);
        params.putString("collection_name", StoryTitle);
        mFirebaseAnalytics.logEvent("viewed_screen_collection", params);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("followstatus details1", followStatus);

        if (TextUtils.isEmpty(followStatus)) {
            followStatus = "0";
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

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                loadNextDataFromApi(page);
            }
        };

        SingleStoryView.addOnScrollListener(scrollListener);
        mparams = new HashMap<>();
        mparams.put("page", "0");
        mparams.put("story_id", StoryId);

        nSingleStoryAdapter = new DetailsmainAdapter(this, mSubstories, storyColour);
        SingleStoryView.setAdapter(nSingleStoryAdapter);

        NetworkCallDetails();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_details);
        swipeRefreshLayout.setOnRefreshListener(this);


        expandedCount = new HashMap<>();

    }

    private void NetworkCallDetails() {

        if (pref.getString(SoupContract.AUTH_TOKEN, null)!=null&&!pref.getString(SoupContract.AUTH_TOKEN, null).isEmpty()) {
            mparams.put("page", "0");
            mparams.put("story_id", StoryId);
            mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams,totalRefresh);
            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            mparams.put("page", "0");
            mparams.put("story_id", StoryId);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams,totalRefresh);
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
            followbutton.setTextColor(Color.parseColor("#B3ffffff"));
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

        totalRefresh="0";

        String page = String.valueOf(offset);

        if (pref.getString(SoupContract.AUTH_TOKEN, null)!=null&&!pref.getString(SoupContract.AUTH_TOKEN, null).isEmpty()) {

            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            mparams.put("page", String.valueOf(offset));
            mparams.put("story_id", StoryId);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams,totalRefresh);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else{

            mparams.put("page",String.valueOf(offset));
            mparams.put("story_id", StoryId);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mparams,totalRefresh);

            try {
                networkutils.getSingleStory();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        }

    @Override
    public void onRefresh() {
        totalRefresh="1";
        swipeRefreshLayout.setRefreshing(true);
        NetworkCallDetails();
    }

    public void startRefreshAdapter(List<Substories> mSubstories, String storyTitle,String StoryId) {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        // nSingleStoryAdapter.refreshData(mSubstories,StoryTitle);

        nSingleStoryAdapter.totalRefreshData((mSubstories),StoryId);
        swipeRefreshLayout.setRefreshing(false);
    }




    public void startAdapter(List<Substories> mSubstories, String StoryTitle,String StoryId) {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        // nSingleStoryAdapter.refreshData(mSubstories,StoryTitle);

        nSingleStoryAdapter.refreshData((mSubstories),StoryId);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "collection_screen");
            mparams.putString("category", "tap"); //(only if possible)
            mFirebaseAnalytics.logEvent("tap_back", mparams);

            if (restartactivitystatus == 1) {

                Intent intent = new Intent(DetailsActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();

                return true;
            } else {
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
    }

    public void onClick(View v) {

        int i = 0; //constant value

        if (v == followbutton || v == tickfollow || v == relativeLayouttick) {
            // TODO : Verify follow event, add collection location if possible
            Bundle params = new Bundle();
            params.putString("screen_name", "collection_screen"); // "myfeed / discover"
            params.putString("collection_id", StoryId);
            params.putString("collection_name", StoryTitle);
            params.putString("category", "tap");

            if (followStatus.equals("") || followStatus.equals("0")) {
                mFirebaseAnalytics.logEvent("tap_add", params);
                HashMap<String, String> mparams = new HashMap<>();
                mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                mparams.put("story_id", StoryId);
                NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(this, mparams);
                followrequest.followRequest(i, fragmenttag);
            } else if (followStatus.equals("1")) {

                mFirebaseAnalytics.logEvent("tap_remove", params);
                HashMap<String, String> mparams = new HashMap<>();
                mparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                mparams.put("story_id", StoryId);
                NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(this, mparams);
                unFollowrequest.unFollowRequest(i, fragmenttag);
            }
        }


    }

    public int getDrawable(String filter) {
        if (filter.equals("Politics & Governments")) {
            return R.drawable.icons8_parliament_filled;

        } else if (filter.equals("Judiciary")) {
            return R.drawable.icons8_scales;

        } else if (filter.equals("Development & Growth Indicators")) {
            return R.drawable.icons8_city_filled;

        } else if (filter.equals("Diplomacy, Defence, War & Terrorism")) {
            return R.drawable.icons8_assault_rifle_filled;
        } else if (filter.equals("Law Enforcement, Crime & Accidents")) {
            return R.drawable.icons8_police_badge_filled;
        } else if (filter.equals("Religion & Philosophy")) {
            return R.drawable.icons8_pray_filled;
        } else if (filter.equals("Human Rights, Social Work & Activism")) {
            return R.drawable.icons8_strike_filled;
        } else if (filter.equals("Sports, Games & Video Gaming")) {
            return R.drawable.icons8_tennis_ball_filled;
        } else if (filter.equals("Entertainment, Lifestyle & Culture")) {
            return R.drawable.icons8_movie_projector_filled;
        } else if (filter.equals("Environment & Nature")) {
            return R.drawable.icons8_earth_element_filled;
        } else if (filter.equals("Healthcare & Medicine")) {
            return R.drawable.icons8_heart_with_pulse_filled;
        } else if (filter.equals("Science & Technology")) {
            return R.drawable.icons8_rocket_filled;
        } else if (filter.equals("Business & Finance")) {
            return R.drawable.icons8_bullish_filled;
        } else if (filter.equals("Work, Career & Skills")) {
            return R.drawable.icons8_university_filled;
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
            if (followStatus.equals("1")) {
                mparams.putString("subscription_state", "added");
            } else {
                mparams.putString("subscription_state", "not added");
            }
            mparams.putString("subcollection_position", String.valueOf(0));
            mFirebaseAnalytics.logEvent("explore_subcollection", mparams);
            expandedCount.put(String.valueOf(0),String.valueOf(0));
        }

    }}
}


       /* SingleStoryView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				Articles mArticle = mSubstories.get(groupPosition).getArticles().get(childPosition);

                String ArticleURL = mArticle.getUrl();

				// CVIPUL Analytics
				// TODO : verify Entity(article) click event
				String entity_id = mArticle.getArticleId();	// added new function in GSON
				String source = mArticle.getSourceName();
				String entity_title = mArticle.getArticleTitle();
				String pub_time = mArticle.getTime();

				Bundle params = new Bundle();
				params.putString("screen_name", "collection_screen"); // "myfeed / discover"
				params.putString("collection_id", StoryId);
				params.putString("follow_status", followStatus);
				params.putString("collection_name", StoryTitle);
				params.putString("category", "tap");
				params.putString("id_entity", entity_id);
				params.putString("type_entity", "article");
				params.putString("name_entity_source", source);
				params.putString("name_entity_title", entity_title);
				params.putString("url_entity", ArticleURL);
				params.putString("time_entity_publish_source", pub_time);
				mFirebaseAnalytics.logEvent("tap_card_entity",params);
				// End Analytics


                Intent intent = new Intent(DetailsActivity.this, ArticleWebViewActivity.class);
                intent.putExtra("ArticleURL", ArticleURL);
                intent.putExtra("substory_id",mSubstories.get(groupPosition).getSubstoryId());
				//CVIPUL Analytics
				intent.putExtra("collection_id", StoryId);
				intent.putExtra("follow_status", followStatus);
				intent.putExtra("collection_name", StoryTitle);
				intent.putExtra("category", "tap");
				intent.putExtra("id_entity", entity_id);
				intent.putExtra("type_entity", "article");
				intent.putExtra("name_entity_source", source);
				intent.putExtra("name_entity_title", entity_title);
				intent.putExtra("time_entity_publish_source", pub_time);
				//End Analytics
                startActivity(intent);


                return true;
            }
        });*/
