package in.thesoup.thesoupstoriesnews.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.EndlessRecyclerView;
import in.thesoup.thesoupstoriesnews.adapters.StoryFeedAdapterMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilswithTokenDiscover;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtil;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Jani on 05-09-2017.
 */


public class DiscoverFragmentMain extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private StoryFeedAdapterMain mStoryfeedAdapter;
    private List<StoryDataMain> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button Discover, MyFeed;
    private EndlessRecyclerView scrollListener;
    private ProgressBar progress;
    private String totalrefresh = "0";
    private Context context;
    private String filter = "", filterfirstname;
    //CVIPUL Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CleverTapAPI cleverTap;
    private int mLastFirstVisibleItem;
    private MotionEvent motionEvent;
    //private boolean mIsScrollingUp;
    // Analytics end


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.getstorieslist, container, false);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        try {
            cleverTap = CleverTapAPI.getInstance(getActivity());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }


        if (pref.getString("filtersdiscover", null) != null && !pref.getString("filtersdiscover", null).isEmpty()) {
            filter = pref.getString("filtersdiscover", null);

            Log.d("filters discover", " :" + filter);
        }


        //removing coma at the end


        if (pref.getString(SoupContract.TOTAL_REFRESH, null) != null && !pref.getString(SoupContract.TOTAL_REFRESH, null).isEmpty()) {
            totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH, null);
        }

        Log.d("totalRefresh Discover", totalrefresh);
        params = new HashMap<>();
        params.put("purpose", "discover");

        params.put("categories", filter);

        progress = (ProgressBar) RootView.findViewById(R.id.progressBar2);
        progress.setVisibility(View.GONE);

        mStoryData = new ArrayList<>();
        StoryView = (RecyclerView) RootView.findViewById(R.id.list_discover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mStoryfeedAdapter = new StoryFeedAdapterMain(mStoryData, getActivity(), 0);
        StoryView.setAdapter(mStoryfeedAdapter);
        StoryView.setLayoutManager(layoutManager);
        StoryView.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) RootView.findViewById(R.id.container_discover);


        //  NetworkCall();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/montserrat-bold.ttf")
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


        swipeRefreshLayout.setOnRefreshListener(DiscoverFragmentMain.this);


        return RootView;
    }

    public void NetworkCall() {

        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {
            params.put("page", "0");
            params.put("purpose", "discover");

            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(getActivity(), mStoryData, params);
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

            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(getActivity(), mStoryData, params);
            networkutilsToken.getFeed(0, totalrefresh);
        }
    }

    @Override
    public void onRefresh() {
        totalrefresh = "1";


        if (pref.getString("filtersdiscover", null) != null && !pref.getString("filtersdiscover", null).isEmpty()) {
            filter = pref.getString("filtersdiscover", null);

            Log.d("filters", " :" + filter);
        }
        scrollListener.changeoffset(0);

        swipeRefreshLayout.setRefreshing(true);

        NetworkCall();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
    }

    public void loadNextDataFromApi(int offset) {

        totalrefresh = "0";

        String Page = String.valueOf(offset);


        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {


            params.put("page", Page);
            params.put("purpose", "discover");


            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(getActivity(), mStoryData, params);


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


            NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(getActivity(), mStoryData, params);


            networkutilsToken.getFeed(0, totalrefresh);

        }
    }


    public void startAdapter(List<StoryDataMain> mStoryData) {
        //StoryView.setAdapter(mStoryfeedAdapter);
        mStoryfeedAdapter.refreshData(mStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            if (getActivity() == null) {
                Log.d("context is null", " at fragment");
            } else {


                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "discover_screen");
                mparams.putString("category", "screen_view");
                mFirebaseAnalytics.logEvent("viewed_screen_discover", mparams);

                HashMap<String,Object> aparams = new HashMap<>();
                aparams.put("screen_name", "discover_screen");
                aparams.put("category", "screen_view");
                cleverTap.event.push("viewed_screen_discover", aparams);


                PrefUtil prefUtil = new PrefUtil(getActivity());

                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh Discover", totalrefresh);

                if (totalrefresh.equals("1")) {

                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", "0");
                    params.put("purpose", "discover");
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);

                    Log.e("Discover story page", params.get("page"));


                    Log.d("filter", filter);
                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                    NetworkUtilswithTokenDiscover networkutilsToken = new NetworkUtilswithTokenDiscover(getActivity(), mStoryData, params);
                    networkutilsToken.getFeed(0, totalrefresh);
                    scrollListener.changeoffset(0);


                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(SoupContract.TOTAL_REFRESH, "0");
                    editor.apply();

                    //prefUtil.saveTotalRefresh("0");

                }
            }
        }


    }


    @Override
    public void onResume() {
        super.onResume();


    }

    public void goToTop() {
        if (StoryView != null) {

            LinearLayoutManager manager =(LinearLayoutManager)StoryView.getLayoutManager();
            int firstvisibleitemposition = manager.findFirstVisibleItemPosition();
            if(firstvisibleitemposition!=0){
                StoryView.scrollToPosition(2);
                StoryView.smoothScrollToPosition(0);
            }else {
                totalrefresh = "1";


                if (pref.getString("filtersdiscover", null) != null && !pref.getString("filtersdiscover", null).isEmpty()) {
                    filter = pref.getString("filtersdiscover", null);

                    Log.d("filters", " :" + filter);
                }
                scrollListener.changeoffset(0);

                swipeRefreshLayout.setRefreshing(true);

                NetworkCall();

            }

        }
    }

    public void startRefreshAdapter(List<StoryDataMain> nStoryData) {
        mStoryData = nStoryData;
        mStoryfeedAdapter.totalRefreshData(nStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);


    }


    public void demo1(int position, String followstatus) {

        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "discover_screen"); // "myfeed / discover"
        mparams.putString("collection_id", mStoryData.get(position).getStoryIdMain());
        mparams.putString("collection_name", mStoryData.get(position).getStoryNameMain());
        mparams.putString("category", "conversion");

        HashMap<String,Object> aparams = new HashMap<>();
        aparams.put("screen_name", "discover_screen"); // "myfeed / discover"
        aparams.put("collection_id", mStoryData.get(position).getStoryIdMain());
        aparams.put("collection_name", mStoryData.get(position).getStoryNameMain());
        aparams.put("category", "conversion");

        if (followstatus.equals("1")) {
            mFirebaseAnalytics.logEvent("add", mparams);
            cleverTap.event.push("add", aparams);
        } else if (followstatus.equals("0")) {
            mFirebaseAnalytics.logEvent("remove", mparams);
            cleverTap.event.push("remove", aparams);
        }


        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SoupContract.TOTAL_REFRESH, "1");
        edit.putString("story_id",mStoryData.get(position).getStoryIdMain());
        edit.apply();


        totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH, null);

        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);


        if (followstatus.equals("1")) {
            Toast.makeText(getActivity(), "Now following the Story", Toast.LENGTH_SHORT).show();
        }
    }


    public void stopProgress() {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }


}

