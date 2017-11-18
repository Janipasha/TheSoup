package in.thesoup.thesoupstoriesnews.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.adapters.CategoryFeedAdapter;
import in.thesoup.thesoupstoriesnews.adapters.StoryFeedAdapterMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilswithTokenDiscover;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.thesoup.thesoupstoriesnews.R.id.back_arrow;

/**
 * Created by Jani on 10-11-2017.
 */

public class CategoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private CategoryFeedAdapter mStoryfeedAdapter;
    private List<StoryDataMain> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button back;
    private EndlessRecyclerView scrollListener;
    private ProgressBar progress;
    private String totalrefresh = "0",category;
    private Context context;
    private String filter = "", filterfirstname;
    private FirebaseAnalytics mFirebaseAnalytics;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CleverTapAPI cleverTap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        try {
            cleverTap = CleverTapAPI.getInstance(this);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
        setContentView(R.layout.category_main_nov);

        if(getIntent()!=null){
            filter = getIntent().getStringExtra("category_id");
            category = getIntent().getStringExtra("category");
        }

        params = new HashMap<>();
        params.put("purpose", "discover");

        params.put("categories", filter);

        progress = (ProgressBar)findViewById(R.id.progressBar2);
        progress.setVisibility(View.GONE);

        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.headertop);
        TextView categoryName = (TextView)findViewById(R.id.header);

        categoryName.setText(category);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> nparams = new HashMap<>();
                nparams.put("screen_name", "category_screen");
                nparams.put("category", "tap");
                nparams.put("type_sort","newly_followed");
                cleverTap.event.push("tap_back_icon");


                finish();
            }
        });

        mStoryData = new ArrayList<>();
        StoryView = (RecyclerView) findViewById(R.id.category_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mStoryfeedAdapter = new CategoryFeedAdapter(mStoryData, this, 0);
        StoryView.setAdapter(mStoryfeedAdapter);
        StoryView.setLayoutManager(layoutManager);
        StoryView.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.category_swipe);

        //  NetworkCall();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/montserrat-regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        scrollListener = new EndlessRecyclerView(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                loadNextDataFromApi(current_page);

            }
        };

        StoryView.addOnScrollListener(scrollListener);
        swipeRefreshLayout.setOnRefreshListener(this);
        NetworkCall();

        HashMap<String,Object> aparams = new HashMap<>();
        aparams.put("screen_name", "category_screen");
        aparams.put("category", "screen_view");
        cleverTap.event.push("viewed_screen_category", aparams);

    }


    public void NetworkCall() {

        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {
            params.put("page", "0");
            params.put("categories", filter);

            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(this, mStoryData, params);
            networkutilsToken.getFeed(0, totalrefresh);
        } else {
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", "0");
            params.put("purpose", "discover");
            params.put("categories", filter);

            Log.e("Discover story page", params.get("page"));
            if (swipeRefreshLayout.isRefreshing()) {
                progress.setVisibility(View.GONE);

            } else {
                progress.setVisibility(View.VISIBLE);

            }
            progress.setProgress(0);


            Log.d("auth_token", pref.getString(SoupContract.AUTH_TOKEN, null));

            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(this, mStoryData, params);
            networkutilsToken.getFeed(0, totalrefresh);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onRefresh() {
        totalrefresh = "1";


        scrollListener.changeoffset(-1);

        swipeRefreshLayout.setRefreshing(true);

        NetworkCall();

    }


    public void loadNextDataFromApi(int offset) {

        totalrefresh = "0";

        String Page = String.valueOf(offset+1);


        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {


            params.put("page", Page);
            params.put("purpose", "discover");


            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(this, mStoryData, params);
            networkutilsToken.getFeed(0, totalrefresh);

        } else {

            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", Page);
            params.put("purpose", "discover");
            params.put("categories", filter);

            Log.e("Discover story page", params.get("page"));

            Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);


            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(this, mStoryData, params);


            networkutilsToken.getFeed(0, totalrefresh);

        }
    }


    public void startAdapter(List<StoryDataMain> mStoryData1) {
        mStoryfeedAdapter.refreshData(mStoryData1);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

    }

    public void startRefreshAdapter(List<StoryDataMain> nStoryData) {
        mStoryData = nStoryData;
        mStoryfeedAdapter.totalRefreshData(nStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);


    }


    public void demo(int position, String followstatus) {

        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "category_screen"); // "myfeed / discover"
        mparams.putString("collection_id", mStoryData.get(position).getStoryIdMain());
        mparams.putString("collection_name", mStoryData.get(position).getStoryNameMain());
        mparams.putString("category", "conversion");

        HashMap<String,Object> aparams = new HashMap<>();
        aparams.put("screen_name","category_screen");
        aparams.put("section_name",mStoryData.get(position).getCategoryName());
        aparams.put("section_id",mStoryData.get(position).getCategoryId());
        aparams.put("position_card_section",position);

        if (followstatus.equals("1")) {
            mFirebaseAnalytics.logEvent("add", mparams);
            cleverTap.event.push("follow", aparams);
        } else if (followstatus.equals("0")) {
            mFirebaseAnalytics.logEvent("remove", mparams);
            cleverTap.event.push("unfollow", aparams);
        }


        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SoupContract.TOTAL_REFRESH, "1");
        edit.putString("story_id",mStoryData.get(position).getStoryIdMain());
        edit.apply();


        totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH, null);

        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);


        if (followstatus.equals("1")) {
            Toast.makeText(this, "Now following the Story", Toast.LENGTH_SHORT).show();
        }
    }


    public void stopProgress() {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }


}






