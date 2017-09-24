package in.thesoupstoriesnews.thesoup.Fragments;

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

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoupstoriesnews.thesoup.Adapters.StoryFeedAdapter;
import in.thesoupstoriesnews.thesoup.Adapters.StoryFeedAdapterMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.StoryDataMain;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithTokenMain;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
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
        private EndlessRecyclerViewScrollListener scrollListener;
        private ProgressBar progress;
        private String totalrefresh="0";
        private Context context;
        private String filter="",filterfirstname;
        //CVIPUL Analytics
        private FirebaseAnalytics mFirebaseAnalytics;
        private SwipeRefreshLayout swipeRefreshLayout;
        private int mLastFirstVisibleItem;
        private MotionEvent motionEvent;
        //private boolean mIsScrollingUp;
        // Analytics end



        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View RootView = inflater.inflate(R.layout.getstorieslist,container,false);

            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());


            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());



            if(pref.getString("filters",null)!=null&&!pref.getString("filters",null).isEmpty()) {
                filter = pref.getString("filters", null);

                Log.d("filters"," :"+filter);
            }



            //removing coma at the end



            if(pref.getString(SoupContract.TOTAL_REFRESH,null)!=null&&!pref.getString(SoupContract.TOTAL_REFRESH,null).isEmpty()){
                totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);}

            Log.d("totalRefresh Discover",totalrefresh);
            params = new HashMap<>();
            params.put("purpose","discover");

            params.put("categories",filter);

            progress = (ProgressBar) RootView.findViewById(R.id.progressBar2);
            progress.setVisibility(View.GONE);

            mStoryData = new ArrayList<>();
            StoryView = (RecyclerView)RootView.findViewById(R.id.list_discover);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mStoryfeedAdapter = new StoryFeedAdapterMain(mStoryData, getActivity(),0);
            StoryView.setAdapter(mStoryfeedAdapter);
            StoryView.setLayoutManager(layoutManager);
            StoryView.setHasFixedSize(true);


            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    loadNextDataFromApi(page);
                }
            };

            StoryView.addOnScrollListener(scrollListener);
            swipeRefreshLayout = (SwipeRefreshLayout)RootView.findViewById(R.id.container_discover);


            NetworkCall();


            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/montserrat-bold.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );

            // CVIPUL Analytics
            // TODO : Verify view Discover feed screen
            Bundle params = new Bundle();
            params.putString("screen_name", "discover_screen");
            params.putString("category", "screen_view");
            mFirebaseAnalytics.logEvent("viewed_screen_discover",params);

      /*  if (!pref.getBoolean("simulateclick1", false)) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    StoryView.findViewHolderForAdapterPosition(0).itemView.performClick();
                }
            }, 1500);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("simulateclick1", true);
            editor.commit();

        }*/


            swipeRefreshLayout.setOnRefreshListener(DiscoverFragmentMain.this);


            return RootView;
        }

        public void NetworkCall(){



            if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {
                params.put("page", "0");
                params.put("purpose","discover");

                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                networkutilsToken.getFeed1(0,totalrefresh);
            } else {
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("page", "0");
                params.put("purpose","discover");
                params.put("categories",filter);
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0);


                Log.d("auth_token", pref.getString(SoupContract.AUTH_TOKEN, null));

                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                networkutilsToken.getFeed1(0,totalrefresh);
            }
        }

        @Override
        public void onRefresh() {
            totalrefresh = "1";
            swipeRefreshLayout.setRefreshing(true);

            NetworkCall();

        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            context = getActivity();
        }

        public void loadNextDataFromApi(int offset) {

            totalrefresh="0";

            String Page = String.valueOf(offset);

            if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {


                params.put("page", Page);
                params.put("purpose","discover");


                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);


                networkutilsToken.getFeed1(0,totalrefresh);

            } else {

                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("page", Page);
                params.put("purpose","discover");
                params.put("categories",filter);

                Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0);

                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);


                networkutilsToken.getFeed1(0,totalrefresh);

            }


        }


        public void startAdapter(List<StoryDataMain> mStoryData) {
            //StoryView.setAdapter(mStoryfeedAdapter);
            mStoryfeedAdapter.refreshData(mStoryData);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

// Dispatch touch event to view



        }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {

            if (getActivity() == null) {
                Log.d("context is null"," at fragment");
            }else {


                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "discover_screen");
                mparams.putString("category", "screen_view");
                mFirebaseAnalytics.logEvent("viewed_screen_discover", mparams);


                PrefUtil prefUtil = new PrefUtil(getActivity());

                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh Discover",totalrefresh);

                if (totalrefresh.equals("1")) {

                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", "0");
                    params.put("purpose","discover");
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);


                    Log.d("filter",filter);

                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                    NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);


                    networkutilsToken.getFeed1(0,totalrefresh);


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

        public void startRefreshAdapter(List<StoryDataMain> nStoryData){
            mStoryData = nStoryData;
            mStoryfeedAdapter.totalRefreshData(nStoryData);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);


        }

       /* @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);

            if(isVisibleToUser) {

                if (getActivity() == null) {
                    Log.d("context is null"," at fragment");
                }else {


                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "discover_screen");
                    mparams.putString("category", "screen_view");
                    mFirebaseAnalytics.logEvent("viewed_screen_discover", mparams);


                    PrefUtil prefUtil = new PrefUtil(getActivity());

                    totalrefresh = prefUtil.getTotalRefresh();
                    Log.d("totalRefresh Discover",totalrefresh);

                    if (totalrefresh.equals("1")) {

                        params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                        params.put("page", "0");
                        params.put("purpose","discover");
                        progress.setVisibility(View.VISIBLE);
                        progress.setProgress(0);

                        for (String name : params.keySet()) {
                            String key = name;
                            String value = params.get(key);
                            Log.d("param values", key + " " + value);
                        }


                        Log.d("filter",filter);

                        Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                        NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                        networkutilsToken.getFeed1(0, totalrefresh);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(SoupContract.TOTAL_REFRESH, "0");
                        editor.apply();

                        //prefUtil.saveTotalRefresh("0");

                    }
                }
            }





        }*/



        public void demo1(int position, String followstatus) {

            // CVIPUL Analytics
            // TODO : Verify follow/unfollow event
            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "discover_screen"); // "myfeed / discover"
		/*
		String Storyname = StoryDataList.get(mposition).getStoryName();
        String mfollowstatus = StoryDataList.get(mposition).getFollowStatus();
        String mString = StoryDataList.get(mposition).getStoryId();
		*/
            mparams.putString("collection_id", mStoryData.get(position).getStoryIdMain());
           // mparams.putString("follow_status", mStoryData.get(position).getFollowStatus());
            mparams.putString("collection_name", mStoryData.get(position).getStoryNameMain());
            mparams.putString("category", "conversion");
            if(followstatus.equals("1")){
                mFirebaseAnalytics.logEvent("add",mparams);
            }
            else if(followstatus.equals("0")){
                mFirebaseAnalytics.logEvent("remove",mparams);
            }
            //End Analytics


            SharedPreferences.Editor edit = pref.edit();
            edit.putString(SoupContract.TOTAL_REFRESH,"1");
            edit.apply();

            totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);

           mStoryData.get(position).changeFollowStatus(followstatus);
            mStoryfeedAdapter.refreshfollowstatus(mStoryData);


            if(followstatus.equals("1")){
                Toast.makeText(getActivity(), "sucessfully followed the story", Toast.LENGTH_SHORT).show();
            }
             }


        public void stopProgress() {

            progress.setProgress(100);
            progress.setVisibility(View.GONE);
        }



    }
